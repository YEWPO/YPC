/***************************************************************************************
* Copyright (c) 2014-2022 Zihao Yu, Nanjing University
*
* NEMU is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
*
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
*
* See the Mulan PSL v2 for more details.
***************************************************************************************/

#include "debug.h"
#include "memory/pmem.h"
#include "utils/cpu.h"

/* We use the POSIX regex functions to process regular expressions.
 * Type 'man regex' for more information about POSIX regex functions.
 */
#include <regex.h>

enum {
  TK_NOTYPE = 256,
  TK_NUM,
  TK_HEXNUM,
  TK_G,
  TK_L,
  TK_GE,
  TK_LE,
  TK_EQ,
  TK_NE,
  TK_AND,
  TK_OR,
  TK_REG,
  TK_DEREF,
  TK_NEG,

  /* TODO: Add more token types */

};

static struct rule {
  const char *regex;
  int token_type;
} rules[] = {

  /* TODO: Add more rules.
   * Pay attention to the precedence level of different rules.
   */
  {">=", TK_GE},        // greater or equal
  {"<=", TK_LE},        // less or equal

  {"!=", TK_NE},        // no equal
  {"==", TK_EQ},        // equal

  {"&&", TK_AND},       // logic and

  {"\\|\\|", TK_OR},    // logic or

  {"!", '!'},           // logic negation
  {"~", '~'},           // bitwise negation

  {"\\*", '*'},         // times
  {"\\/", '/'},         // divide
                        
  {"\\+", '+'},         // plus
  {"-", '-'},           // minus

  {">", TK_G},          // greater
  {"<", TK_L},          // less

  {"&", '&'},           // bitwise and

  {"\\^", '^'},         // bitwise nor

  {"\\|", '|'},         // bitwise or

  {" +", TK_NOTYPE},    // spaces
  {"0x[0-9a-f]+", TK_HEXNUM}, // hex number
  {"[0-9]+", TK_NUM},   // number
  {"\\(", '('},         // left bracket
  {"\\)", ')'},         // right bracket
  {"\\$[\\$0-9a-z]+", TK_REG}, // registers
};

#define NR_REGEX ARRLEN(rules)

static regex_t re[NR_REGEX] = {};

/* Rules are used for many times.
 * Therefore we compile them only once before any usage.
 */
void init_regex() {
  int i;
  char error_msg[128];
  int ret;

  for (i = 0; i < NR_REGEX; i ++) {
    ret = regcomp(&re[i], rules[i].regex, REG_EXTENDED);
    if (ret != 0) {
      regerror(ret, &re[i], error_msg, 128);
      Assert(0, "regex compilation failed: %s\n%s", error_msg, rules[i].regex);
    }
  }
}

typedef struct token {
  int type;
  char str[32];
} Token;

static Token tokens[3000] __attribute__((used)) = {};
static int nr_token __attribute__((used))  = 0;

static bool make_token(char *e) {
  int position = 0;
  int i;
  regmatch_t pmatch;

  nr_token = 0;

  while (e[position] != '\0') {
    /* Try all rules one by one. */
    for (i = 0; i < NR_REGEX; i ++) {
      if (regexec(&re[i], e + position, 1, &pmatch, 0) == 0 && pmatch.rm_so == 0) {
        char *substr_start = e + position;
        int substr_len = pmatch.rm_eo;

        Log("match rules[%d] = \"%s\" at position %d with len %d: %.*s",
            i, rules[i].regex, position, substr_len, substr_len, substr_start);

        position += substr_len;

        switch (rules[i].token_type) {
          case '!':
          case '~':
          case '*':
          case '/':
          case '+':
          case '-':
          case TK_G:
          case TK_L:
          case TK_GE:
          case TK_LE:
          case TK_EQ:
          case TK_NE:
          case '&':
          case '^':
          case '|':
          case TK_AND:
          case TK_OR:
          case '(':
          case ')':
            tokens[nr_token++].type = rules[i].token_type;
            break;
          case TK_HEXNUM:
          case TK_NUM:
          case TK_REG:
            tokens[nr_token].type = rules[i].token_type;
            if (substr_len < 32) {
              strncpy(tokens[nr_token].str, substr_start, substr_len);
              tokens[nr_token++].str[substr_len] = '\0';
            } else {
              Assert(0, "Expr length larger than 32");
            }
            break;
          case TK_NOTYPE:
            break;
          default:
            Assert(0, "Token need to do!");
        }

        break;
      }
    }

    if (i == NR_REGEX) {
      printf("no match at position %d\n%s\n%*.s^\n", position, e, position, "");
      return false;
    }
  }

  return true;
}

static bool check_parentheses(int l, int r) {
  if (tokens[l].type == '(' && tokens[r].type == ')') {
    int tag = 1;
    int i;

    for (i = l + 1; i < r; i++) {
      if (tokens[i].type == '(') {
        tag++;
      } else if (tokens[i].type == ')') {
        tag--;
      }
      if (tag == 0) {
        return false;
      }
    }
  } else {
    return false;
  }

  return true;
}

static int match_parentheses(int startp, int endp) {
  /* got the next token position that not in a match_parentheses */
  if (tokens[startp].type != '(') {
    return startp + 1;
  }

  int tag = 0;
  while (startp <= endp) {
    if (tokens[startp].type == '(') {
      tag++;
    } else if (tokens[startp].type == ')') {
      tag--;
    }

    if (tag == 0) {
      return startp + 1;
    }
    startp++;
  }

  return startp;
}

static word_t expr_calc(int l, int r, bool *success) {
  if (l > r) {
    /* bad expression */
    *success = false;
    return 0;
  }

  word_t val = 0;
  if (l == r) {
    if (tokens[l].type == TK_NUM) {
      /* got a number */
      val = strtoull(tokens[l].str, NULL, 10);

      *success = true;
      return val;
    } else if (tokens[l].type == TK_HEXNUM) {
      /* got a hex number */
      val = strtoull(tokens[l].str, NULL, 16);

      *success = true;
      return val;
    } else if (tokens[l].type == TK_REG) {
      val = isa_reg_str2val(tokens[l].str + 1, success);
      return val;
    } else {
      /* bad expression */
      *success = false;
      return 0;
    }
  }

  if (check_parentheses(l, r) == true) {
    /* chechk if it is around by a matched parentheses */
    val = expr_calc(l + 1, r - 1, success);
    if (*success == true) {
      return val;
    } else {
      return 0;
    }
  }

  /* find main operation */
  int op_position = -1;
  int op_level = 15;

  int i;
  for (i = l; i <= r; i = match_parentheses(i, r)) {
    switch (tokens[i].type) {
      case TK_OR:
        if (1 <= op_level) {
          op_level = 1;
          op_position = i;
        }
        break;
      case TK_AND:
        if (2 <= op_level) {
          op_level = 2;
          op_position = i;
        }
        break;
      case '|':
        if (3 <= op_level) {
          op_level = 3;
          op_position = i;
        }
        break;
      case '^':
        if (4 <= op_level) {
          op_level = 4;
          op_position = i;
        }
        break;
      case '&':
        if (5 <= op_level) {
          op_level = 5;
          op_position = i;
        }
        break;
      case TK_EQ:
      case TK_NE:
        if (6 <= op_level) {
          op_level = 6;
          op_position = i;
        }
        break;
      case TK_G:
      case TK_L:
      case TK_GE:
      case TK_LE:
        if (7 <= op_level) {
          op_level = 7;
          op_position = i;
        }
        break;
      case '+':
      case '-':
        if (8 <= op_level) {
          op_level = 8;
          op_position = i;
        }
        break;
      case '*':
      case '/':
        if (9 <= op_level) {
          op_level = 9;
          op_position = i;
        }
        break;
      case '!':
      case '~':
      case TK_DEREF:
      case TK_NEG:
        if (10 <= op_level) {
          op_level = 10;
          op_position = i;
        }
        break;
      default:
        break;
    }
  }

  if (op_position == -1) {
    /* not found, bad expression */
    *success = false;
    return 0;
  }

  if (tokens[op_position].type == '!') {
    if (op_position != l 
        && (tokens[op_position - 1].type == TK_NUM
          || tokens[op_position - 1].type == TK_HEXNUM
          || tokens[op_position - 1].type == TK_REG)) {
      *success = false;
      return 0;
    }
    val = expr_calc(op_position + 1, r, success);
    if (*success == false) {
      return 0;
    }

    *success = true;
    val = !val;
    tokens[op_position].type = TK_HEXNUM;
    sprintf(tokens[op_position].str, FMT_WORD, val);
    return expr_calc(l, op_position, success);
  }
  if (tokens[op_position].type == '~') {
    if (op_position != l 
        && (tokens[op_position - 1].type == TK_NUM
          || tokens[op_position - 1].type == TK_HEXNUM
          || tokens[op_position - 1].type == TK_REG)) {
      *success = false;
      return 0;
    }
    val = expr_calc(op_position + 1, r, success);
    if (*success == false) {
      return 0;
    }

    *success = true;
    val = ~val;
    tokens[op_position].type = TK_HEXNUM;
    sprintf(tokens[op_position].str, FMT_WORD, val);
    return expr_calc(l, op_position, success);
  }
  if (tokens[op_position].type == TK_DEREF) {
    if (op_position != l 
        && (tokens[op_position - 1].type == TK_NUM
          || tokens[op_position - 1].type == TK_HEXNUM
          || tokens[op_position - 1].type == TK_REG)) {
      *success = false;
      return 0;
    }
    val = expr_calc(op_position + 1, r, success);
    if (*success == false) {
      return 0;
    }

    *success = true;
    val = paddr_read(val, 8);
    tokens[op_position].type = TK_HEXNUM;
    sprintf(tokens[op_position].str, FMT_WORD, val);
    return expr_calc(l, op_position, success);
  }
  if (tokens[op_position].type == TK_NEG) {
    if (op_position != l 
        && (tokens[op_position - 1].type == TK_NUM
          || tokens[op_position - 1].type == TK_HEXNUM
          || tokens[op_position - 1].type == TK_REG)) {
      *success = false;
      return 0;
    }
    val = expr_calc(op_position + 1, r, success);
    if (*success == false) {
      return 0;
    }

    *success = true;
    val = -val;
    tokens[op_position].type = TK_HEXNUM;
    sprintf(tokens[op_position].str, FMT_WORD, val);
    return expr_calc(l, op_position, success);
  }

  word_t val1 = expr_calc(l, op_position - 1, success);
  if (*success == false) {
    return 0;
  }
  if (tokens[op_position].type == TK_OR) {
    if (val1 != 0) {
      *success = true;
      return 1;
    }
  } else if (tokens[op_position].type == TK_AND) {
    if (val1 == 0) {
      *success = true;
      return 0;
    }
  }

  word_t val2 = expr_calc(op_position + 1, r, success);
  if (*success == false) {
    return 0;
  }
  if (tokens[op_position].type == TK_OR) {
    if (val2 != 0) {
      *success = true;
      return 1;
    } else {
      *success = true;
      return 0;
    }
  } else if (tokens[op_position].type == TK_AND) {
    if (val2 == 0) {
      *success = true;
      return 0;
    } else {
      *success = true;
      return 1;
    }
  }

  /* got left and right expression value, connect them */
  switch (tokens[op_position].type) {
    case '*':
      val = val1 * val2;
      break;
    case '/':
      val = val1 / val2;
      break;
    case '+':
      val = val1 + val2;
      break;
    case '-':
      val = val1 - val2;
      break;
    case TK_G:
      val = val1 > val2;
      break;
    case TK_L:
      val = val1 < val2;
      break;
    case TK_GE:
      val = val1 >= val2;
      break;
    case TK_LE:
      val = val1 <= val2;
      break;
    case TK_EQ:
      val = val1 == val2;
      break;
    case TK_NE:
      val = val1 != val2;
      break;
    case '&':
      val = val1 & val2;
      break;
    case '^':
      val = val1 ^ val2;
      break;
    case '|':
      val = val1 | val2;
      break;
    default:
      Assert(0, "Operation need to do!");
  }

  *success = true;
  return val;
}

static bool check_deref(int pos) {
  if (pos == 0) {
    return true;
  }

  if (tokens[pos - 1].type != TK_NUM
      && tokens[pos - 1].type != TK_HEXNUM
      && tokens[pos - 1].type != TK_REG) {
    return true;
  }

  return false;
}

static bool check_neg(int pos) {
  if (pos == 0) {
    return true;
  }

  if (tokens[pos - 1].type != TK_NUM
      && tokens[pos - 1].type != TK_HEXNUM
      && tokens[pos - 1].type != TK_REG) {
    return true;
  }

  return false;
}

word_t expr(char *e, bool *success) {
  if (!make_token(e)) {
    *success = false;
    return 0;
  }

  for (int i = 0; i < nr_token; i++) {
    if (tokens[i].type == '*' && check_deref(i)) {
      tokens[i].type = TK_DEREF;
    }
  }

  for (int i = 0; i < nr_token; i++) {
    if (tokens[i].type == '-' && check_neg(i)) {
      tokens[i].type = TK_NEG;
    }
  }

  *success = true;
  return expr_calc(0, nr_token - 1, success);
}

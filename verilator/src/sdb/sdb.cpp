#include <readline/history.h>
#include <readline/readline.h>

#include <cstring>
#include <malloc.h>

#include "macro.h"
#include "simulate/simulator.h"
#include "utils/cpu.h"

static int is_batch_mode = false;

/* We use the `readline' library to provide more flexibility to read from stdin. */
static char *rl_gets() {
  static char *line_read = NULL;

  if (line_read) {
    free(line_read);
    line_read = NULL;
  }

  line_read = readline("(npc) ");

  if (line_read && *line_read) {
    add_history(line_read);
  }

  return line_read;
}

static int cmd_c(char *args) {
  cpu_exec(-1);
  return 0;
}

static int cmd_q(char *args) {
  npc_state.state = NPC_QUIT;
  return -1;
}

static int cmd_si(char *args) {
  /* extract the first argument */
  char *arg = strtok(NULL, " ");

  if (arg == NULL) {
    /*
     * no argument given
     * default step one instruction exactly
     */
    cpu_exec(1);
  } else {
    uint64_t step_time;
    char *endptr;
    step_time = strtoull(arg, &endptr, 10);

    if (*endptr == '\0') {
      /*
       * got a valid value
       * execute step_time times
       */
      cpu_exec(step_time);
    } else {
      /* got a invalid value */
      printf("invalid value %s\n", arg);
    }
  }

  return 0;
}

static void info_r(char *args) {
  dump_isa();
}

static struct {
  const char *name;
  const char *description;
  void (*handler) (char *);
} info_opt_table[] = {
  {"r", "List of integer registers and their contents, for selected stack frame.", info_r},

  /* TODO: Add more options */

};

#define NR_INFO_OPT ARRLEN(info_opt_table)

static int cmd_info(char *args) {
  /* extract the first argument */
  char *arg = strtok(NULL, " ");
  int i;

  if (arg == NULL) {
    /*
     * no argument given
     * output help
     */
 
    for (i = 0; i < NR_INFO_OPT; i++) {
      printf("%s - %s\n", info_opt_table[i].name, info_opt_table[i].description);
    }
  } else {
    /*
     * check arg
     * and execute
     */
    for (i = 0; i < NR_INFO_OPT; i++) {
      if (strcmp(info_opt_table[i].name, arg) == 0) {
        info_opt_table[i].handler(args);
        break;
      }
    } 

    if (i == NR_INFO_OPT) {
      printf("Unknown option '%s'\n", arg);
    }
  }

  return 0;
}

static int cmd_help(char *args);

static struct {
  const char *name;
  const char *description;
  int (*handler)(char *);
} cmd_table[] = {
    {"help", "Display information about all supported commands", cmd_help},
    {"c", "Continue the execution of the program", cmd_c},
    {"q", "Exit NPC", cmd_q},
    {"si", "Step one or [N] instruction exactly.", cmd_si},
    {"info", "Generic command for showing things about the program being debugged.", cmd_info},

    /* TODO: Add more commands */

};

#define NR_CMD ARRLEN(cmd_table)

static int cmd_help(char *args) {
  /* extract the first argument */
  char *arg = strtok(NULL, " ");
  int i;

  if (arg == NULL) {
    /* no argument given */
    for (i = 0; i < NR_CMD; i++) {
      printf("%s - %s\n", cmd_table[i].name, cmd_table[i].description);
    }
  } else {
    for (i = 0; i < NR_CMD; i++) {
      if (strcmp(arg, cmd_table[i].name) == 0) {
        printf("%s - %s\n", cmd_table[i].name, cmd_table[i].description);
        return 0;
      }
    }
    printf("Unknown command '%s'\n", arg);
  }
  return 0;
}

void sdb_set_batch_mode() { is_batch_mode = true; }

void sdb_mainloop() {
  if (is_batch_mode) {
    cmd_c(NULL);
    return;
  }

  for (char *str; (str = rl_gets()) != NULL;) {
    char *str_end = str + strlen(str);

    /* extract the first token as the command */
    char *cmd = strtok(str, " ");
    if (cmd == NULL) {
      continue;
    }

    /* treat the remaining string as the arguments,
     * which may need further parsing
     */
    char *args = cmd + strlen(cmd) + 1;
    if (args >= str_end) {
      args = NULL;
    }

    int i;
    for (i = 0; i < NR_CMD; i++) {
      if (strcmp(cmd, cmd_table[i].name) == 0) {
        if (cmd_table[i].handler(args) < 0) {
          return;
        }
        break;
      }
    }

    if (i == NR_CMD) {
      printf("Unknown command '%s'\n", cmd);
    }
  }
}

void sdb_init() {}

#include <elf.h>
#include "simulate/simulator.h"

static Elf64_Ehdr ehdr;

static uint64_t shdr_offset;
static uint16_t shdr_num;
static Elf64_Shdr *shdr;

static uint64_t sym_offset;
static Elf64_Sym *symtab;
static uint16_t sym_num;

static uint16_t strtab_idx;
static uint64_t strtab_offset;
static uint64_t strtab_size;
static char *strtab;

struct Func_info {
  uint32_t name;
  uint64_t addr;
  uint64_t size;
  struct Func_info *next;
} *func_list;

static void add_func(uint32_t name, uint64_t addr, uint64_t size) {
  struct Func_info *node = (struct Func_info*)malloc(sizeof(struct Func_info));
  node->name = name;
  node->addr = addr;
  node->size = size;
  node->next = func_list;

  func_list = node;
}

void func_sym_init(char *filename) {
  Log("filename: %s", filename);

  FILE *fp = fopen(filename, "r");
  assert(fp != 0);

  // read ELF header and got segment header offset and number
  assert(fread(&ehdr, sizeof(Elf64_Ehdr), 1, fp) != 0);
  shdr_offset = ehdr.e_shoff;
  shdr_num = ehdr.e_shnum;
  
  // read segment headers
  shdr = (Elf64_Shdr*)malloc(shdr_num * sizeof(Elf64_Shdr));
  fseek(fp, shdr_offset, SEEK_SET);
  assert(fread(shdr, sizeof(Elf64_Shdr), shdr_num, fp) != 0);

  for (int i = 0; i < shdr_num; ++i) {
    // search .symtab
    if (shdr[i].sh_type == SHT_SYMTAB) {
      // get .strtab from symtab.link
      strtab_idx = shdr[i].sh_link;
      strtab_offset = shdr[strtab_idx].sh_offset;
      strtab_size = shdr[strtab_idx].sh_size;
      strtab = (char*)malloc(strtab_size);
      fseek(fp, strtab_offset, SEEK_SET);
      assert(fread(strtab, sizeof(char), strtab_size, fp) != 0);

      //got symbal table offset and number
      sym_offset = shdr[i].sh_offset;
      sym_num = shdr[i].sh_size / sizeof(Elf64_Sym);

      // read symbol table
      symtab = (Elf64_Sym*)malloc(sym_num * sizeof(Elf64_Sym));
      fseek(fp, sym_offset, SEEK_SET);
      assert(fread(symtab, sizeof(Elf64_Sym), sym_num, fp) != 0);

      break;
    }
  }

  for (int i = 0; i < sym_num; ++i) {
    if (ELF64_ST_TYPE(symtab[i].st_info) == STT_FUNC) {
      // add sym to link array
      add_func(symtab[i].st_name, symtab[i].st_value, symtab[i].st_size);
    }
  }

  fclose(fp);
}

#define JAR_MASK 0x6f
#define JARL_MASK 0x67
#define RET_MASK 0x8067

// is jump instruction?
static int ijump(uint32_t inst) {
  return ((inst & JAR_MASK) == JAR_MASK) || ((inst & JARL_MASK) == JARL_MASK);
}

// is ret instruction
static int iret(uint32_t inst) {
  return inst == RET_MASK;
}

// is a function entry address?
static int is_func_entry(uint64_t pc) {
  struct Func_info *ptr = func_list;

  while (ptr != NULL) {
    if (ptr->addr == pc) {
      return 1;
    }
    ptr = ptr->next;
  }

  return 0;
}

// rd is zero register?
static int is_rd_x0(int inst) {
  return (inst & 0xf80) == 0x0;
}

enum {T_CALL, T_RET};

struct Ftrace_node {
  int type;
  uint64_t pc;
  uint64_t dnpc;
  uint32_t name;
  int ret; // need return?
  struct Ftrace_node *next;
} *fnode_head, *fnode_tail;

// get function name by address
static uint32_t search_func_name(uint64_t pc) {
  struct Func_info *ptr = func_list;
  
  while (ptr != NULL) {
    if (pc >= ptr->addr
        && pc < ptr->addr + ptr->size) {
      return ptr->name;
    }

    ptr = ptr->next;
  }

  Assert(0, "Function name not found!" FMT_WORD, pc);
}

void ftrace_inst(Decode *_this) {
  if (iret(_this->inst)) {
    // this is a ret instruction
    struct Ftrace_node *node = (struct Ftrace_node*)malloc(sizeof(struct Ftrace_node));
    
    node->type = T_RET;
    node->pc = _this->pc;
    node->dnpc = _this->dnpc;
    node->name = search_func_name(node->dnpc);
    node->next = NULL;

    // add to link list
    if (fnode_tail == NULL) {
      fnode_head = node;
      fnode_tail = node;
    } else {
      fnode_tail->next = node;
      fnode_tail = node;
    }
  } else if (ijump(_this->inst)
      && is_func_entry(_this->dnpc)) {
    // this is a call instruction
    struct Ftrace_node *node = (struct Ftrace_node*)malloc(sizeof(struct Ftrace_node));
    
    // judge if it needs return
    if (!is_rd_x0(_this->inst)) {
      node->ret = 1;
    }

    node->type = T_CALL;
    node->pc = _this->pc;
    node->dnpc = _this->dnpc;
    node->name = search_func_name(node->dnpc);
    node->next = NULL;

    // add to link list
    if (fnode_tail == NULL) {
      fnode_head = node;
      fnode_tail = node;
    } else {
      fnode_tail->next = node;
      fnode_tail = node;
    }
  }
}

void ftrace_print() {
  struct Ftrace_node *ptr = fnode_head;
  int deep = 0; // deep info
  int nret = 0; // how many calls have no return

  printf("frame trace info:\n");

  while (ptr != NULL) {
    printf(FMT_WORD ":", ptr->pc);
    if (ptr->type == T_RET) {
      deep--;
      deep -= nret;
      nret = 0;
    }
    for (int i = 0; i < deep; ++i) {
      printf("  ");
    }
    if (ptr->type == T_CALL) {
      deep++;
      if (ptr->ret == 0) {
        nret++;
      }
      printf("call");
    } else {
      printf("ret");
    }
    printf("[" ANSI_FMT("%s", ANSI_FG_GREEN) "@" ANSI_FMT(FMT_WORD, ANSI_FG_RED) "]\n", strtab + ptr->name, ptr->dnpc);

    ptr = ptr->next;
  }
}

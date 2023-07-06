#include "utils/cpu.h"
#include <svdpi.h>
#include <verilated_dpi.h>
#include "VTop__Dpi.h"

NPCState npc_state;

uint64_t *gpr_ptr;

CPU_state cpu;

void set_gpr_ptr(const svOpenArrayHandle regs) {
  gpr_ptr = (uint64_t *)(((VerilatedDpiOpenVar *)regs)->datap());

  for (int i = 0; i < 32; ++i) {
    cpu.gpr[i] = gpr_ptr[i];
  }
}

void set_pc_val(const svLogicVecVal* pc_val) {
  cpu.pc = *((uint64_t *)pc_val);
}

const char *regs[] = {
  "$0", "ra", "sp", "gp", "tp", "t0", "t1", "t2",
  "s0", "s1", "a0", "a1", "a2", "a3", "a4", "a5",
  "a6", "a7", "s2", "s3", "s4", "s5", "s6", "s7",
  "s8", "s9", "s10", "s11", "t3", "t4", "t5", "t6"
};

#define NR_REG ARRLEN(regs)

void dump_isa() {
  for (int i = 0; i < 32; ++i) {
    printf("%s: \t%016lx\n", regs[i], cpu.gpr[i]);
  }

  printf("pc: \t%016lx\n", cpu.pc);
}

word_t isa_reg_str2val(const char *s, bool *success) {
  int i;

  for (i = 0; i < NR_REG; i++) {
    if (strcmp(s, regs[i]) == 0) {
      *success = true;
      return cpu.gpr[i];
    }
  }

  if (strcmp(s, "pc") == 0) {
    *success = true;
    return cpu.pc;
  }

  *success = false;
  return 0;
}

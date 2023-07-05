#include "utils/cpu.h"
#include <svdpi.h>
#include <verilated_dpi.h>
#include "VTop__Dpi.h"

NPCState npc_state;

uint64_t riscv64_pc;

uint64_t *gpr_ptr;

uint64_t riscv64_regs[32];

void set_gpr_ptr(const svOpenArrayHandle regs) {
  gpr_ptr = (uint64_t *)(((VerilatedDpiOpenVar *)regs)->datap());

  for (int i = 0; i < 32; ++i) {
    riscv64_regs[i] = gpr_ptr[i];
  }
}

void set_pc_val(const svLogicVecVal* pc_val) {
  riscv64_pc = *((uint64_t *)pc_val);
}

const char *regs[] = {
  "$0", "ra", "sp", "gp", "tp", "t0", "t1", "t2",
  "s0", "s1", "a0", "a1", "a2", "a3", "a4", "a5",
  "a6", "a7", "s2", "s3", "s4", "s5", "s6", "s7",
  "s8", "s9", "s10", "s11", "t3", "t4", "t5", "t6"
};

void dump_isa() {
  for (int i = 0; i < 32; ++i) {
    printf("%s: \t%016lx\n", regs[i], riscv64_regs[i]);
  }

  printf("pc: \t%016lx\n", riscv64_pc);
}

#include <svdpi.h>
#include <verilated_dpi.h>
#include "VTop__Dpi.h"

#include "common.h"
#include "isa/isa.h"

uint64_t *gpr_ptr;

uint64_t riscv64_regs[32];

void set_gpr_ptr(const svOpenArrayHandle regs) {
  gpr_ptr = (uint64_t *)(((VerilatedDpiOpenVar *)regs)->datap());

  for (int i = 0; i < 32; ++i) {
    riscv64_regs[i] = gpr_ptr[i];
  }
}

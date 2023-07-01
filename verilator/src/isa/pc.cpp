#include "svdpi.h"
#include "common.h"
#include "VTop.h"
#include "VTop__Dpi.h"

#include "isa/isa.h"

uint64_t riscv64_pc;

void set_pc_val(const svLogicVecVal* pc_val) {
  riscv64_pc = *((uint64_t *)pc_val);
}

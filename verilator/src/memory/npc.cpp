#include "VTop__Dpi.h"
#include <svdpi.h>
#include <verilated_dpi.h>
#include "VTop.h"

#include "common.h"
#include "memory/pmem.h"

extern VTop *top;

void npc_mem_ifetch(const long long addr, int *inst, const svLogic en) {
  static bool pre_clock = false;
  if (top->clock == pre_clock) return;
  pre_clock = top->clock;
  if (top->clock == false) return;

  *inst = en ? paddr_read(addr, 4) : 0x13;
}

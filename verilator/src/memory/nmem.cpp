#include "VTop__Dpi.h"
#include <svdpi.h>
#include <verilated_dpi.h>
#include "VTop.h"

#include "common.h"
#include "memory/pmem.h"

#define ADDR_MASK (~0x7ull)
#define ALIGN_ADDR(addr) (addr & ADDR_MASK)

extern VTop *top;

void nmem_ifetch(const long long addr, int *inst, const svLogic en) {
  static bool pre_clock = false;
  if (top->clock == pre_clock) return;
  pre_clock = top->clock;
  if (top->clock == false) return;

  *inst = en ? paddr_read(addr, 4) : 0x13;
}

void nmem_read(const long long addr, long long *r_data, const long long mask) {
  static bool pre_clock = false;
  if (top->clock == pre_clock) return;
  pre_clock = top->clock;
  if (top->clock == false) return;

  *r_data = mask ? paddr_read(addr, 8) & mask : 0;
}

void nmem_write(const long long addr, const long long w_data, const long long mask) {
  static bool pre_clock = false;
  if (top->clock == pre_clock) return;
  pre_clock = top->clock;
  if (top->clock == false) return;

  if (mask) paddr_write(addr, 8, (paddr_read(addr, 8) & ~mask) | (w_data & mask));
}

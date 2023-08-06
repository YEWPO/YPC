#include "VTop__Dpi.h"
#include <svdpi.h>
#include <verilated_dpi.h>
#include "VTop.h"

#include "common.h"
#include "macro.h"
#include "memory/pmem.h"

#define ADDR_MASK (~0x7ull)
#define ALIGN_ADDR(addr) (addr & ADDR_MASK)

extern VTop *top;

void nmem_ifetch(const long long addr, long long *r_data, const svLogic r_en) {
  static bool pre_clock = false;
  if (top->clock == pre_clock) return;
  pre_clock = top->clock;
  if (top->clock == false) return;

  if (!r_en) { *r_data = 0; return; };
  *r_data = pmem_read(addr);
}

void nmem_read(const long long addr, long long *r_data, const svLogic r_en) {
  static bool pre_clock = false;
  if (top->clock == pre_clock) return;
  pre_clock = top->clock;
  if (top->clock == false) return;

  if (!r_en) { *r_data = 0; return; };
  if (likely(in_pmem(addr))) {
    *r_data = pmem_read(addr);
  }
}

void nmem_write(const long long addr, const long long w_data, const long long mask, const svLogic w_en) {
  static bool pre_clock = false;
  if (top->clock == pre_clock) return;
  pre_clock = top->clock;
  if (top->clock == false) return;

  if (!w_en) return;
  if (likely(in_pmem(addr))) {
    pmem_write(addr, w_data, mask);
  }
}

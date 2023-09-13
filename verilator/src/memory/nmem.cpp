#include "VTop__Dpi.h"
#include <svdpi.h>
#include <verilated_dpi.h>
#include "VTop.h"

#include "common.h"
#include "macro.h"
#include "memory/pmem.h"
#include "utils/cpu.h"
#include "device/mmio.h"

extern CPU_state cpu;

extern VTop *top;

static void out_of_bound(paddr_t addr) {
  Assert(0, "address = " FMT_PADDR " is out of bound of pmem [" FMT_PADDR ", " FMT_PADDR "] at pc = " FMT_WORD,
      addr, PMEM_LEFT, PMEM_RIGHT, cpu.pc);
}

void nmem_ifetch(const long long addr, long long *r_data) {
  if (addr < PMEM_LEFT || addr > PMEM_RIGHT) {
    Log("address = " FMT_PADDR " is out of bound of pmem [" FMT_PADDR ", " FMT_PADDR "] at pc = " FMT_WORD,
      (paddr_t)addr, PMEM_LEFT, PMEM_RIGHT, cpu.pc);
    *r_data = 0;
    return;
  }
  *r_data = pmem_read(addr);
}

void nmem_read(const long long addr, long long *r_data) {
  if (likely(in_pmem(addr))) {
    *r_data = pmem_read(addr);
    return;
  }
  IFDEF(CONFIG_DEVICE, *r_data = mmio_read(addr); return);
  Log("address = " FMT_PADDR " is out of bound of pmem [" FMT_PADDR ", " FMT_PADDR "] at pc = " FMT_WORD,
    (paddr_t)addr, PMEM_LEFT, PMEM_RIGHT, cpu.pc);
  *r_data = 0;
}

void nmem_write(const long long addr, const long long w_data, const char mask) {
  if (likely(in_pmem(addr))) {
    pmem_write(addr, w_data, mask);
    return;
  }
  IFDEF(CONFIG_DEVICE, mmio_write(addr, w_data, mask); return);
  Log("address = " FMT_PADDR " is out of bound of pmem [" FMT_PADDR ", " FMT_PADDR "] at pc = " FMT_WORD,
    (paddr_t)addr, PMEM_LEFT, PMEM_RIGHT, cpu.pc);
}

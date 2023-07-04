#include "VTop__Dpi.h"
#include <svdpi.h>
#include <verilated_dpi.h>
#include "VTop.h"

#include "common.h"
#include "memory/vmem.h"

#define WRITE 1
#define READ 0

extern VTop *top;
static bool pre_clock;

void mem(const svLogic mem_en, const svLogic w_en, const svLogic signed_en,
         const long long addr, const long long w_data,
         const long long r_mask, long long *r_data) {
  if (top->clock == 1) {
    if (top->clock == pre_clock) {
      return;
    }
    pre_clock = top->clock;
  } else {
    pre_clock = top->clock;
    return;
  }

  if (mem_en == 0) {
    *((uint64_t *)r_data) = addr;
    return;
  }

  Log(ANSI_FMT("MEM, %016llx, %016llx", ANSI_FG_RED), addr, r_mask);

  uint64_t mask = r_mask;

  if (w_en == 1) {
    uint64_t data = w_data;

    switch (mask) {
      case 0x0ffl:
        vaddr_write(addr, 1, data);
        break;
      case 0x0ffffl:
        vaddr_write(addr, 2, data);
        break;
      case 0x0ffffffffl:
        vaddr_write(addr, 4, data);
        break;
      case 0xffffffffffffffffl:
        vaddr_write(addr, 8, data);
        break;
      default:
        break;
    }
  } else {
    uint64_t data;

    switch (mask) {
      case 0x0ffl:
        data = vaddr_read(addr, 1);
        if (signed_en == 1) data = (int8_t)data;
        break;
      case 0x0ffffl:
        data = vaddr_read(addr, 2);
        if (signed_en == 1) data = (int16_t)data;
        break;
      case 0x0ffffffffl:
        data = vaddr_read(addr, 4);
        if (signed_en == 1) data = (int32_t)data;
        break;
      case 0xffffffffffffffffl:
        data = vaddr_read(addr, 8);
        break;
      default:
        data = 0;
        break;
    }

    *((uint64_t *)r_data) = data;
  }
}

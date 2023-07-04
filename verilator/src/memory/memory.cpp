#include "VTop__Dpi.h"
#include <svdpi.h>
#include <verilated_dpi.h>
#include "VTop.h"

#include "common.h"
#include "memory/vmem.h"

#define WRITE 1
#define READ 0

#define SIGNED(sig) (sig == 1)

extern VTop *top;
static bool pre_clock;

void mem(const svLogic mem_en, const svLogic w_en, const svLogic signed_en,
         const svLogicVecVal *addr, const svLogicVecVal *w_data,
         const svLogicVecVal *r_mask, svLogicVecVal *r_data) {
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
    *((uint64_t *)r_data) = *((uint64_t *)addr);
    return;
  }

  Log(ANSI_FMT("MEM", ANSI_FG_RED));

  uint64_t mask = *((uint64_t *)r_mask);

  if (w_en) {
    uint64_t data = *((uint64_t *)w_data);

    switch (mask) {
      case 0x0ffl:
        vaddr_write(*((uint64_t *)addr), 1, data);
        break;
      case 0x0ffffl:
        vaddr_write(*((uint64_t *)addr), 2, data);
        break;
      case 0x0ffffffffl:
        vaddr_write(*((uint64_t *)addr), 4, data);
        break;
      case 0xffffffffffffffffl:
        vaddr_write(*((uint64_t *)addr), 8, data);
        break;
      default:
        break;
    }
  } else {
    uint64_t data;

    switch (mask) {
      case 0x0ffl:
        data = vaddr_read(*((uint64_t *)addr), 1);
        if (signed_en == 1) data = (int8_t)data;
        break;
      case 0x0ffffl:
        data = vaddr_read(*((uint64_t *)addr), 2);
        if (signed_en == 1) data = (int16_t)data;
        break;
      case 0x0ffffffffl:
        data = vaddr_read(*((uint64_t *)addr), 4);
        if (signed_en == 1) data = (int32_t)data;
        break;
      case 0xffffffffffffffffl:
        data = vaddr_read(*((uint64_t *)addr), 8);
        break;
      default:
        data = 0;
        break;
    }

    *((uint64_t *)r_data) = data;
  }
}

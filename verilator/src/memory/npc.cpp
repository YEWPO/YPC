#include "VTop__Dpi.h"
#include <svdpi.h>
#include <verilated_dpi.h>
#include "VTop.h"

#include "common.h"
#include "memory/pmem.h"

void npc_mem_ifetch(const long long addr, long long *inst) {
  *inst = paddr_read(addr, 4);
}

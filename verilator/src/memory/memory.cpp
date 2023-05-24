#include <svdpi.h>
#include "VTop__Dpi.h"

#include "common.h"
#include "memory/vmem.h"

#define WRITE true
#define READ false

long long memory_io(long long addr, long long w_data, svLogic op, int len) {
  if (op == WRITE) {
    vaddr_write(addr, len, w_data);
  }

  if (op == READ) {
    return vaddr_read(addr, len);
  }

  return 0;
}

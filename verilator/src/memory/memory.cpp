#include <svdpi.h>
#include "VTop__Dpi.h"

#include "common.h"
#include "memory/vmem.h"

#define WRITE 1
#define READ 0

long long memory_io(long long addr, long long w_data, svLogic op, svLogic m_en, int len) {
  if (m_en == 0) {
    return 0;
  }

  if (op == WRITE) {
    vaddr_write(addr, len, w_data);
  }

  if (op == READ) {
    return vaddr_read(addr, len);
  }

  return 0;
}

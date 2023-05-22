#include <svdpi.h>
#include "VTop__Dpi.h"

#include "common.h"
#include "memory/vmem.h"

#define WRITE true
#define READ false

word_t memory_io(vaddr_t addr, word_t w_data, bool op, int len) {
  if (op == WRITE) {
    vaddr_write(addr, len, w_data);
  }

  if (op == READ) {
    return vaddr_read(addr, len);
  }

  return 0;
}

#include "memory/pmem.h"
#include "memory/vmem.h"

word_t vaddr_ifetch(vaddr_t addr, int len) {
  Log("inst's addr: 0x%016lx", addr);
  return paddr_read(addr, len);
}

word_t vaddr_read(vaddr_t addr, int len) {
  return paddr_read(addr, len);
}

void vaddr_write(vaddr_t addr, int len, word_t data) {
  paddr_write(addr, len, data);
}

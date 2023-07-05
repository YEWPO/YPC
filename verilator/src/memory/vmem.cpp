#include "memory/pmem.h"
#include "memory/vmem.h"

extern long img_size;

word_t vaddr_ifetch(vaddr_t addr, int len) {
  Assert((addr - CONFIG_MBASE) <= img_size, "Instruction address is out of bound!");
  
  word_t inst = paddr_read(addr, len);
  Log("inst: 0x%016lx 0x%08lx", addr, inst);
  return inst;
}

word_t vaddr_read(vaddr_t addr, int len) {
  return paddr_read(addr, len);
}

void vaddr_write(vaddr_t addr, int len, word_t data) {
  paddr_write(addr, len, data);
}

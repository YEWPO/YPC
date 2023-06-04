#include "memory/pmem.h"
#include "memory/vmem.h"

extern long img_size;

word_t vaddr_ifetch(vaddr_t addr, int len) {
  Assert((addr - CONFIG_MBASE) <= img_size, "Instruction address is out of bound!");
  
  Log("inst's addr: 0x%016lx", addr);
  word_t inst = paddr_read(addr, len);
  Log("instruction is 0x%08lx", inst);
  return inst;
}

word_t vaddr_read(vaddr_t addr, int len) {
  Log("memory read addr: 0x%016lx", addr);
  Log("memory read len: %d", len);
  return paddr_read(addr, len);
}

void vaddr_write(vaddr_t addr, int len, word_t data) {
  Log("memory write addr: 0x%016lx", addr);
  Log("memory write len: %d", len);
  Log("memory write data: 0x%016lx", data);
  paddr_write(addr, len, data);
}

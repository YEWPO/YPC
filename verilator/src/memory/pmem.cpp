#include "memory/host.h"
#include "memory/pmem.h"

static uint8_t __attribute__(( aligned(4096) )) pmem[CONFIG_MSIZE];

uint8_t* guest_to_host(paddr_t paddr) { return pmem + paddr - CONFIG_MBASE; }
paddr_t host_to_guest(uint8_t *haddr) { return haddr - pmem + CONFIG_MBASE; }

static word_t pmem_read(paddr_t addr, int len) {
  Assert(addr >= CONFIG_MBASE, "Invalid address");
  word_t ret = host_read(guest_to_host(addr), len);
  return ret;
}

static void pmem_write(paddr_t addr, int len, word_t data) {
  Assert(addr >= CONFIG_MBASE, "Invalid address");
  host_write(guest_to_host(addr), len, data);
}

word_t paddr_read(paddr_t addr, int len) {
  return pmem_read(addr, len);
}

void paddr_write(paddr_t addr, int len, word_t data) {
  pmem_write(addr, len, data);
  return;
}

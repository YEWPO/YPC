#ifndef _PMEM_H
#define _PMEM_H

#include "common.h"

#define CONFIG_MSIZE 0x8000000
#define CONFIG_MBASE 0x80000000

uint8_t* guest_to_host(paddr_t paddr);
paddr_t host_to_guest(uint8_t *haddr);

static inline bool in_pmem(paddr_t addr) {
  return addr - CONFIG_MBASE < CONFIG_MSIZE;
}

word_t paddr_read(paddr_t addr, int len);
void paddr_write(paddr_t addr, int len, word_t data);

#endif

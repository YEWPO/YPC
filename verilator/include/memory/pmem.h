#ifndef _PMEM_H
#define _PMEM_H

#include "common.h"

#define PMEM_LEFT  ((paddr_t)CONFIG_MBASE)
#define PMEM_RIGHT ((paddr_t)CONFIG_MBASE + CONFIG_MSIZE - 1)
#define RESET_VECTOR (PMEM_LEFT + CONFIG_PC_RESET_OFFSET)
#define ADDR_MASK (~0x7ull)

#define PAGE_SIZE (1 << 12)
#define PAGE_MASK (PAGE_SIZE - 1)

uint8_t* guest_to_host(paddr_t paddr);
paddr_t host_to_guest(uint8_t *haddr);

static inline bool in_pmem(paddr_t addr) {
  return addr - CONFIG_MBASE < CONFIG_MSIZE;
}

void init_mem();
word_t pmem_read(paddr_t addr);
void pmem_write(paddr_t addr, word_t data, char mask);

#endif

#ifndef _MMIO_H
#define _MMIO_H

#include <common.h>

word_t mmio_read(paddr_t addr);
void mmio_write(paddr_t addr, word_t data, char mask);

#endif

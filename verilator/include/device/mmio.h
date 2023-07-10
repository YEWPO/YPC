#ifndef _MMIO_H
#define _MMIO_H

#include "common.h"

word_t mmio_read(paddr_t addr, int len);
void mmio_write(paddr_t addr, int len, word_t data);

#endif

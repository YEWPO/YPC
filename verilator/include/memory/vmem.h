#ifndef _VMEM_H
#define _VMEM_H

#include "common.h"

word_t vaddr_ifetch(vaddr_t addr, int len);
word_t vaddr_read(vaddr_t addr, int len);
void vaddr_write(vaddr_t addr, int len, word_t data);

#endif

#ifndef _DIFFTEST_H
#define _DIFFTEST_H

#include "common.h"

enum { DIFFTEST_TO_DUT, DIFFTEST_TO_REF };

extern void (*difftest_memcpy)(paddr_t addr, void *buf, size_t n, bool direction);
extern void (*difftest_regcpy)(void *dut, bool direction);
extern void (*difftest_exec)(uint64_t n);

#endif

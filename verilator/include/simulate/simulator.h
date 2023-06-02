#ifndef _SIMULATOR_H
#define _SIMULATOR_H

#include <stdint.h>

void simulator_init();
void simulator_destory();
void reset(uint64_t n = 5);
void step_clock_round(uint64_t n = 1);

#endif

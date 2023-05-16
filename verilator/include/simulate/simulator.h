#ifndef _SIMULATOR_H
#define _SIMULATOR_H

#include <stdint.h>

void start_simulate();
void reset(uint64_t n);
void step_clock_round(uint64_t n);

#endif

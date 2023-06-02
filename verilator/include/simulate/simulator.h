#ifndef _SIMULATOR_H
#define _SIMULATOR_H

#include <cstdint>

void simulator_init();
void simulator_destroy();
void cpu_exec(uint64_t n = 1);

#endif

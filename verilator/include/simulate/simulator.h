#ifndef _SIMULATOR_H
#define _SIMULATOR_H

#include "common.h"

typedef struct Decode {
  vaddr_t pc;
  vaddr_t snpc;
  vaddr_t dnpc;
  uint32_t inst;
  IFDEF(CONFIG_ITRACE, char logbuf[128]);
} Decode;

void init_simulator();
void simulator_destroy();
void cpu_exec(uint64_t n = 1);

#endif

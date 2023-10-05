#ifndef _INSTPOOL_H
#define _INSTPOOL_H

#include "common.h"

typedef struct {
  long long pc;
  int inst;
  long long dnpc;
  bool skip_diff;
} InstExeInfo;

void push_inst(const long long pc, const int inst, const long long dnpc, const bool device_op);
void skip_difftest();
InstExeInfo *pop_inst();
bool test_inst_avail();

#endif

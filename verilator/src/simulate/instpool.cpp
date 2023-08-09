#include "VTop.h"
#include <svdpi.h>
#include "VTop__Dpi.h"

#include "simulate/instpool.h"

#define NR_INST_POOL 8
#define NEXT_POOL_IDX(x) ((x + 1) % NR_INST_POOL)
#define FAR_NEXT_POOL_IDX(x) ((x + 2) % NR_INST_POOL)

InstExeInfo inst_exe_pool[NR_INST_POOL];

static int inst_avail_idx = 0;
static int inst_parse_idx = 0;

bool test_inst_avail() {
  return inst_parse_idx != inst_avail_idx;
}

void push_inst(const long long pc, const int inst, const long long dnpc) {
  inst_avail_idx = NEXT_POOL_IDX(inst_avail_idx);
  inst_exe_pool[inst_avail_idx].pc = pc;
  inst_exe_pool[inst_avail_idx].inst = inst;
  inst_exe_pool[inst_avail_idx].dnpc = dnpc;
}

void skip_difftest() {
  inst_exe_pool[FAR_NEXT_POOL_IDX(inst_avail_idx)].skip_diff = true;
}

InstExeInfo *pop_inst() {
  inst_parse_idx = NEXT_POOL_IDX(inst_parse_idx);
  return &inst_exe_pool[inst_parse_idx];
}

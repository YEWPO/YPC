#ifndef _CPU_H
#define _CPU_H

#include "common.h"

enum {NPC_RUNNING, NPC_STOP, NPC_END, NPC_ABORT, NPC_QUIT};

typedef struct {
  int state;
  vaddr_t halt_pc;
  uint32_t halt_ret;
} NPCState;

typedef struct {
  uint64_t gpr[32];
  uint64_t pc;
} CPU_state;

extern NPCState npc_state;

void dump_isa();
word_t isa_reg_str2val(const char *s, bool *success);

#endif

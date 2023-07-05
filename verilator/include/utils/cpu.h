#ifndef _CPU_H
#define _CPU_H

#include "common.h"

enum {NPC_RUNNING, NPC_STOP, NPC_END, NPC_ABORT, NPC_QUIT};

typedef struct {
  int state;
  vaddr_t halt_pc;
  uint32_t halt_ret;
} NPCState;

extern NPCState npc_state;

void dump_isa();

#endif

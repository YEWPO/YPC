#include "difftest.h"
#include "utils/cpu.h"

extern CPU_state cpu;

static bool isa_difftest_checkregs(CPU_state *ref_r, vaddr_t pc) {
  bool flag = true;

  for (int i = 0; i < 32; ++i) {
    if (cpu.gpr[i] != ref_r->gpr[i]) {
      flag = false;
      break;
    }
  }

  if (cpu.pc != ref_r->pc) {
    flag = false;
  }

  if (!flag) {
    printf(ANSI_FMT("Something wrong at pc " FMT_WORD, ANSI_FG_YELLOW) "\n", pc);
  }

  return true;
}

static void checkregs(CPU_state *ref, vaddr_t pc) {
  if (!isa_difftest_checkregs(ref, pc)) {
    npc_state.state = NPC_ABORT;
    npc_state.halt_pc = pc;
    dump_isa();
  }
}

void difftest_step(uint64_t pc) {
  CPU_state ref;

  difftest_exec(1);
  difftest_regcpy(&ref, DIFFTEST_TO_DUT);
}

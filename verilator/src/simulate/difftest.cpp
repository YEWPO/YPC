#include "simulate/difftest.h"
#include "utils/cpu.h"

#include <dlfcn.h>

extern CPU_state cpu;
void (*difftest_exec)(uint64_t n);
void (*difftest_memcpy)(paddr_t addr, void *buf, size_t n, bool direction);
void (*difftest_regcpy)(void *dut, bool direction);
void *dl_handle;

void dl_init(const char *dl_file) {
  dl_handle = dlopen(dl_file, RTLD_LAZY);
  if (!dl_handle) {
    fprintf(stderr, "%s: %s\n",dl_file, dlerror());
    exit(EXIT_FAILURE);
  }

  difftest_exec = (void (*)(uint64_t))dlsym(dl_handle, "difftest_exec");
  difftest_memcpy = (void (*)(paddr_t, void *, size_t, bool))dlsym(dl_handle, "difftest_memcpy");
  difftest_regcpy = (void (*)(void *, bool))dlsym(dl_handle, "difftest_regcpy");
}

static bool isa_difftest_checkregs(CPU_state *ref_r, vaddr_t pc) {
  extern const char *regs[];

  for (int i = 0; i < 32; ++i) {
    if(!difftest_check_reg(regs[i], pc, ref_r->gpr[i], cpu.gpr[i])) {
      return false;
    }
  }

  if(!difftest_check_reg("pc", pc, ref_r->pc, cpu.pc)) {
    return false;
  }

  return true;
}

static void checkregs(CPU_state *ref, vaddr_t pc) {
  if (!isa_difftest_checkregs(ref, pc)) {
    npc_state.state = NPC_ABORT;
    npc_state.halt_pc = pc;
  }
}

void difftest_step(uint64_t pc) {
  CPU_state ref;

  difftest_exec(1);
  difftest_regcpy(&ref, DIFFTEST_TO_DUT);

  checkregs(&ref, pc);
}

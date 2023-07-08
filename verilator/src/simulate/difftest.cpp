#include "difftest.h"
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

  return flag;
}

static void checkregs(CPU_state *ref, vaddr_t pc) {
  if (!isa_difftest_checkregs(ref, pc)) {
    for (int i = 0; i < 32; ++i) {
      printf("ref %d\t0x%016lx\n", i, ref->gpr[i]);
    }
    printf("ref pc \t0x%016lx\n", ref->pc);

    npc_state.state = NPC_ABORT;
    npc_state.halt_pc = pc;
    dump_isa();
  }
}

void difftest_step(uint64_t pc) {
  CPU_state ref;

  difftest_exec(1);
  difftest_regcpy(&ref, DIFFTEST_TO_DUT);

  checkregs(&ref, pc);
}

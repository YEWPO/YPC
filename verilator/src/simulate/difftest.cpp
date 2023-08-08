#include "simulate/difftest.h"
#include "utils/cpu.h"
#include "memory/pmem.h"

#include <dlfcn.h>

extern CPU_state cpu;
void (*difftest_exec)(uint64_t n);
void (*difftest_memcpy)(paddr_t addr, void *buf, size_t n, bool direction);
void (*difftest_regcpy)(void *dut, bool direction);
void *dl_handle;

void dl_init(const char *dl_file, long size) {
  dl_handle = dlopen(dl_file, RTLD_LAZY);
  if (!dl_handle) {
    fprintf(stderr, "%s: %s\n",dl_file, dlerror());
    exit(EXIT_FAILURE);
  }

  difftest_exec = (void (*)(uint64_t))dlsym(dl_handle, "difftest_exec");
  difftest_memcpy = (void (*)(paddr_t, void *, size_t, bool))dlsym(dl_handle, "difftest_memcpy");
  difftest_regcpy = (void (*)(void *, bool))dlsym(dl_handle, "difftest_regcpy");

  difftest_memcpy(CONFIG_MBASE, guest_to_host(CONFIG_MBASE), size, DIFFTEST_TO_REF);
  difftest_regcpy(&cpu, DIFFTEST_TO_REF);

  if (difftest_exec) Log("Already dynamic link %s", dl_file);
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

#ifdef CONFIG_DIFFTEST

static bool is_skip_ref = false;
static int skip_dut_nr_inst = 0;

void difftest_skip_ref() {
  is_skip_ref = true;
  skip_dut_nr_inst = 0;
}

void difftest_skip_dut(int nr_ref, int nr_dut) {
  skip_dut_nr_inst += nr_dut;

  while (nr_ref -- > 0) {
    difftest_exec(1);
  }
}

void difftest_step(vaddr_t pc, vaddr_t npc) {
  CPU_state ref_r;

  if (skip_dut_nr_inst > 0) {
    difftest_regcpy(&ref_r, DIFFTEST_TO_DUT);
    if (ref_r.pc == npc) {
      skip_dut_nr_inst = 0;
      checkregs(&ref_r, npc);
      return;
    }
    skip_dut_nr_inst --;
    if (skip_dut_nr_inst == 0)
      Assert(0, "can not catch up with ref.pc = " FMT_WORD " at pc = " FMT_WORD, ref_r.pc, pc);
    return;
  }

  if (is_skip_ref) {
    // to skip the checking of an instruction, just copy the reg state to reference design
    difftest_regcpy(&cpu, DIFFTEST_TO_REF);
    is_skip_ref = false;
    return;
  }

  difftest_exec(1);
  difftest_regcpy(&ref_r, DIFFTEST_TO_DUT);

  checkregs(&ref_r, pc);
}
#else
void init_difftest(char *ref_so_file, long img_size, int port) { }
#endif

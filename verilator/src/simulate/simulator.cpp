#include "VTop.h"
#include <svdpi.h>
#include "VTop__Dpi.h"

#include <verilated.h>
#include <verilated_vcd_c.h>

#include "common.h"
#include "simulate/instpool.h"
#include "simulate/simulator.h"
#include "utils/cpu.h"
#include "utils/systime.h"
#include "sdb/watchpoint.h"

#include "simulate/difftest.h"

#define MAX_INST_TO_PRINT 10

static uint64_t g_timer;
static uint64_t g_nr_guest_inst;

extern CPU_state cpu;

static bool g_print_step = false;

VerilatedContext *context;
VerilatedVcdC *vcd;
VTop *top;

void ebreak(const long long pc) {
  Log(ANSI_FMT("EBREAK", ANSI_FG_RED));
  difftest_skip_ref();
  npc_state.halt_pc = pc;
  npc_state.halt_ret = cpu.gpr[10];
  npc_state.state = NPC_END;
}

void invalid(const long long pc) {
  Log(ANSI_FMT("INVALID", ANSI_FG_RED));
  difftest_skip_ref();
  npc_state.state = NPC_ABORT;
  npc_state.halt_ret = -1;
  npc_state.halt_pc = pc;
}

void inst_finish(const long long pc, const int inst, const long long dnpc) {
#define NOP 0x13
  if (top->reset == true || inst == NOP) return;
  push_inst(pc, inst, dnpc);
}

static void step_one();

static void reset(uint64_t n = 5) {
  Log("reseting the NPC");

  top->reset = 1;
  while (n--) step_one();
  top->reset = 0;
}

void init_simulator() {
  Log("Initializing simulator...");

  context = new VerilatedContext;

  Verilated::traceEverOn(true);
  vcd = new VerilatedVcdC;
  top = new VTop;

#ifdef CONFIG_WTRACE_COND
  top->trace(vcd, 99);
  vcd->open("../build/sim.vcd");
#endif

  reset();

  cpu.pc = CONFIG_MBASE;
}

void simulator_destroy() {
  vcd->close();

  delete top;
  delete vcd;
  delete context;
}

static void step_one() {
  context->timeInc(1);
  top->clock = 1;
  top->eval();
#ifdef CONFIG_WTRACE_COND
  vcd->dump(context->time());
#endif

  context->timeInc(1);
  top->clock = 0;
  top->eval();
#ifdef CONFIG_WTRACE_COND
  vcd->dump(context->time());
#endif
}

#ifdef CONFIG_FTRACE_COND
void ftrace_inst(Decode *_this);
void ftrace_print();
#endif

#ifdef CONFIG_ITRACE_COND
void iring_print();
void add2iring(Decode *_this);
#endif

static void trace_and_difftest(Decode *_this) {
#ifdef CONFIG_ITRACE_COND
  add2iring(_this);
#endif
#ifdef CONFIG_FTRACE_COND
  ftrace_inst(_this);
#endif

#ifdef CONFIG_ITRACE_COND
  log_write("%s\n", _this->logbuf);
#endif
  if (g_print_step) { IFDEF(CONFIG_ITRACE, puts(_this->logbuf)); }
  IFDEF(CONFIG_DIFFTEST, difftest_step(_this->pc, _this->dnpc));

#ifdef CONFIG_WATCHPOINT
  bool newtag;
  check_watchpoints(&newtag);
  if (newtag == true) {
    npc_state.state = NPC_STOP;
  }
#endif
}

static void exec_one(Decode *s) {
  InstExeInfo *cur = pop_inst();
  s->pc = cur->pc;
  s->snpc = s->pc + 4;
  s->inst = cur->inst;
  cpu.pc = cur->dnpc;
  if (cur->skip_diff) {
    difftest_skip_ref();
  }
#ifdef CONFIG_ITRACE
  char *p = s->logbuf;
  p += snprintf(p, sizeof(s->logbuf), FMT_WORD ":", s->pc);
  int ilen = s->snpc - s->pc;
  int i;
  uint8_t *inst = (uint8_t *)&s->inst;
  for (i = ilen - 1; i >= 0; i --) {
    p += snprintf(p, 4, " %02x", inst[i]);
  }
  int ilen_max = MUXDEF(CONFIG_ISA_x86, 8, 4);
  int space_len = ilen_max - ilen;
  if (space_len < 0) space_len = 0;
  space_len = space_len * 3 + 1;
  memset(p, ' ', space_len);
  p += space_len;

  void disassemble(char *str, int size, uint64_t pc, uint8_t *code, int nbyte);
  disassemble(p, s->logbuf + sizeof(s->logbuf) - p,
      s->snpc, (uint8_t *)&s->inst, ilen);
#endif
}

static void check_inst() {
  if (test_inst_avail()) {
    Decode s;
    exec_one(&s);
    g_nr_guest_inst++;
    trace_and_difftest(&s);
  }
}

static void step_clock(uint64_t n) {
  while (n--) {
    check_inst();

    step_one();

    if (npc_state.state != NPC_RUNNING) {
      check_inst();
      break;
    }
  }
}

static void statistic() {
#define NUMBERIC_FMT "%'" PRIu64
  Log("host time spent = " NUMBERIC_FMT " us", g_timer);
  Log("total guest instructions = " NUMBERIC_FMT, g_nr_guest_inst);
  if (g_timer > 0) Log("simulation frequency = " NUMBERIC_FMT " inst/s", g_nr_guest_inst * 1000000 / g_timer);
  else Log("Finish running in less than 1 us and can not calculate the simulation frequency");
}

void assert_fail_msg() {
  dump_isa();
  statistic();
}

void cpu_exec(uint64_t n) {
  g_print_step = (n < MAX_INST_TO_PRINT);

  switch (npc_state.state) {
    case NPC_END: case NPC_ABORT:
      printf("Program execution has ended. To restart the program, exit NPC and run again.\n");
      return;
    default: npc_state.state = NPC_RUNNING;
  }

  uint64_t start_time = get_time();

  step_clock(n);

  uint64_t end_time = get_time();

  g_timer += end_time - start_time;

  switch (npc_state.state) {
    case NPC_RUNNING: npc_state.state = NPC_STOP; break;

    case NPC_END: case NPC_ABORT:
      Log("npc: %s at pc = " FMT_WORD,
          (npc_state.state == NPC_ABORT ? ANSI_FMT("ABORT", ANSI_FG_RED) :
           (npc_state.halt_ret == 0 ? ANSI_FMT("HIT GOOD TRAP", ANSI_FG_GREEN) :
            ANSI_FMT("HIT BAD TRAP", ANSI_FG_RED))),
          npc_state.halt_pc);

    if (npc_state.state == NPC_ABORT || npc_state.halt_ret != 0) {
#ifdef CONFIG_ITRACE_COND
      iring_print();
#endif
#ifdef CONFIG_FTRACE_COND
      ftrace_print();
#endif
    }

    case NPC_QUIT:
      statistic();
  }
}

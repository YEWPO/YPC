#include "VTop.h"
#include <svdpi.h>
#include "VTop__Dpi.h"

#include <verilated.h>
#include <verilated_vcd_c.h>

#include "common.h"
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

void ebreak() {
  static bool pre_clock = false;
  if (top->clock == pre_clock) return;
  pre_clock = top->clock;
  if (top->clock == false) return;

  Log(ANSI_FMT("EBREAK", ANSI_FG_RED));
  npc_state.halt_pc = cpu.pc;
  npc_state.halt_ret = cpu.gpr[10];
  npc_state.state = NPC_END;
}

void invalid() {
  static bool pre_clock = false;
  if (top->clock == pre_clock) return;
  pre_clock = top->clock;
  if (top->clock == false) return;

  Log(ANSI_FMT("INVALID", ANSI_FG_RED));
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

// ========== itrace ===============

#ifdef CONFIG_ITRACE_COND

typedef struct Iring {
  char log[128];
} Iring;

#define IRING_SIZE 36
#define IRING_NEXT(p) ((p + 1) % IRING_SIZE)
#define IRING_PREV(p) ((p - 1 + IRING_SIZE) % IRING_SIZE)
static Iring iring[IRING_SIZE] = {};
static int iring_head = 0;

static void iring_print() {
  printf("instruct execute info:\n");

  int ptr = iring_head;
  while (ptr != IRING_PREV(iring_head)) {
    if (strlen(iring[ptr].log) > 0) {
      printf("    ");
      puts(iring[ptr].log);
    }
    ptr = IRING_NEXT(ptr);
  }

  printf("--> ");
  puts(iring[ptr].log);
}

static void add2iring(Decode *_this) {
  strcpy(iring[iring_head].log, _this->logbuf);
  iring_head = IRING_NEXT(iring_head);
}

#endif

// ============ itrace ============

#ifdef CONFIG_FTRACE_COND

void ftrace_inst(Decode *_this);
void ftrace_print();

#endif

void difftest_step(uint64_t pc);

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
  IFDEF(CONFIG_DIFFTEST, difftest_step(_this->pc));

#ifdef CONFIG_WATCHPOINT
  bool newtag;
  check_watchpoints(&newtag);
  if (newtag == true) {
    npc_state.state = NPC_STOP;
  }
#endif
}

static void inst_itrace(Decode *s) {
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

static void step_clock(uint64_t n) {
  Decode s;

  while (n--) {
    // top->io_inst = vaddr_ifetch(top->io_pc, 4);
    // s.inst = top->io_inst;
    // s.pc = top->io_pc;
    // s.snpc = s.pc + 4;

    step_one();
    // s.dnpc = top->io_pc;
    inst_itrace(&s);

    // g_nr_guest_inst++;
    trace_and_difftest(&s);

    if (npc_state.state != NPC_RUNNING) {
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

#include "VTop.h"
#include <svdpi.h>
#include "VTop__Dpi.h"

#include <verilated.h>
#include <verilated_vcd_c.h>

#include "common.h"
#include "isa/isa.h"
#include "simulate/simulator.h"
#include "memory/vmem.h"
#include "utils/cpu.h"
#include "utils/timer.h"

static uint64_t g_timer;
static uint64_t g_nr_guest_inst;

extern uint64_t riscv64_regs[32];
extern uint64_t riscv64_pc;

VerilatedContext *context;
VerilatedVcdC *vcd;
VTop *top;

static void reset(uint64_t n = 5);

void ebreak() {
  npc_state.halt_pc = riscv64_pc;
  npc_state.halt_ret = riscv64_regs[10];
  npc_state.state = NPC_END;
}

void simulator_init() {
  Log("Initializing simulator...");

  context = new VerilatedContext;

  Verilated::traceEverOn(true);
  vcd = new VerilatedVcdC;
  top = new VTop;

  top->trace(vcd, 99);
  vcd->open("../build/sim.vcd");

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
  vcd->dump(context->time());

  context->timeInc(1);
  top->clock = 0;
  top->eval();
  vcd->dump(context->time());
}

static void step_clock_round(uint64_t n) {
  while (n--) {
    step_one();
  }
}

static void exec_inst(uint64_t n) {
  while (n--) {
    top->io_inst = vaddr_ifetch(top->io_pc, 4);

    step_one();
    g_nr_guest_inst++;

    if (npc_state.state != NPC_RUNNING) {
      break;
    }
  }
}


static void statistic() {
  Log("host time spent = %ld us", g_timer);
  Log("total guest instructions = %ld" , g_nr_guest_inst);
  if (g_timer > 0) Log("simulation frequency = %ld inst/s", g_nr_guest_inst * 1000000 / g_timer);
  else Log("Finish running in less than 1 us and can not calculate the simulation frequency");
}

void cpu_exec(uint64_t n) {
  switch (npc_state.state) {
    case NPC_END: case NPC_ABORT:
      printf("Program execution has ended. To restart the program, exit NPC and run again.\n");
      return;
    default: npc_state.state = NPC_RUNNING;
  }

  uint64_t start_time = get_time();

  exec_inst(n);

  uint64_t end_time = get_time();

  g_timer += end_time - start_time;

  switch (npc_state.state) {
    case NPC_RUNNING: npc_state.state = NPC_STOP; break;

    case NPC_END: case NPC_ABORT:
      Log("npc: %s at pc = " "%016lx",
          (npc_state.state == NPC_ABORT ? ANSI_FMT("ABORT", ANSI_FG_RED) :
           (npc_state.halt_ret == 0 ? ANSI_FMT("HIT GOOD TRAP", ANSI_FG_GREEN) :
            ANSI_FMT("HIT BAD TRAP", ANSI_FG_RED))),
          npc_state.halt_pc);
    case NPC_QUIT:
      statistic();
  }
}

static void reset(uint64_t n) {
  Log("reseting...");
  
  top->reset = 1;

  step_clock_round(n);

  top->reset = 0;
}

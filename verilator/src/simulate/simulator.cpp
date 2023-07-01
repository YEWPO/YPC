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

VerilatedContext *context;
VerilatedVcdC *vcd;
VTop *top;

static void reset(uint64_t n = 5);

void ebreak() {
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

  dump_isa();
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

    if (npc_state.state != NPC_RUNNING) {
      break;
    }
  }
}

void cpu_exec(uint64_t n) {
  switch (npc_state.state) {
    case NPC_END: case NPC_ABORT:
      printf("Program execution has ended. To restart the program, exit NPC and run again.\n");
      return;
    default: npc_state.state = NPC_RUNNING;
  }


  exec_inst(n);

  switch (npc_state.state) {
    case NPC_RUNNING: npc_state.state = NPC_STOP; break;
  }
}

static void reset(uint64_t n) {
  Log("reseting...");
  
  top->reset = 1;

  step_clock_round(n);

  top->reset = 0;
}

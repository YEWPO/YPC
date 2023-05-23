#include "VTop.h"
#include <svdpi.h>
#include "VTop__Dpi.h"


#include <verilated.h>
#include <verilated_vcd_c.h>

#include "common.h"
#include "simulate/simulator.h"
#include "memory/vmem.h"

VerilatedContext *context;
VerilatedVcdC *vcd;
VTop *top;

static bool halt = false;

void ebreak() {
  halt = true;
}

static void simulator_init() {
  context = new VerilatedContext;

  Verilated::traceEverOn(true);
  vcd = new VerilatedVcdC;
  top = new VTop;

  top->trace(vcd, 99);
  vcd->open("../build/sim.vcd");
}

static void simulator_detroy() {
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

void step_clock_round(uint64_t n = 1) {
  while (n--) {
    step_one();
  }
}

void reset(uint64_t n = 5) {
  top->reset = 1;

  step_clock_round(n);

  top->reset = 0;
}

void start_simulate() {
  simulator_init();

  reset();

  while (!context->gotFinish()) {
    top->io_inst = vaddr_ifetch(top->io_pc, 4);
    step_clock_round();
    Log("inst: 0x%08lx", top->io_out);

    if (halt) {
      break;
    }
  }

  simulator_detroy();
}

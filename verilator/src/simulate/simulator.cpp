#include "VTop.h"

#include <verilated.h>
#include <verilated_vcd_c.h>

#include "common.h"
#include "simulate/simulator.h"

VerilatedContext *context;
VerilatedVcdC *vcd;
VTop *top;

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

static void step_clock_round(uint64_t n = 1) {
  while (n--) {
    context->timeInc(1);
    top->clock = 1;
    top->eval();
    vcd->dump(context->time());

    context->timeInc(1);
    top->clock = 0;
    top->eval();
    vcd->dump(context->time());
  }
}

static void reset(uint64_t n = 5) {
  top->reset = 1;

  step_clock_round(n);

  top->reset = 0;
}

void start_simulate() {
  simulator_init();

  reset();

  while (!context->gotFinish()) {
    step_clock_round();

    if (context->time() > 1000) {
      break;
    }
  }

  simulator_detroy();
}

#include "VTop.h"
#include "VTop__Dpi.h"

#include <verilated.h>
#include <verilated_vcd_c.h>

#include <cassert>
#include <cstdint>
#include <cstdio>
#include <cstdlib>
#include <ctime>

VerilatedContext *context;
VerilatedVcdC *vcd;
VTop *top;

void reset(int n) {
  top->reset = 1;

  while (n--) {
    context->timeInc(1);
    top->clock ^= 1;
    top->eval();
    vcd->dump(context->time());
  }

  top->reset = 0;
}

#define MSIZE 0x8000000

static uint8_t mem[MSIZE] = {
    0x93, 0x00, 0x00, 0x02, 0x93, 0x00, 0x00, 0x02, 0x93, 0x00, 0x00, 0x02,
    0x93, 0x00, 0x00, 0x02, 0x93, 0x00, 0x00, 0x02, 0x73, 0x00, 0x10, 0x00,
};

static bool halt = false;

void ebreak() { halt = true; }

static uint32_t paddr_read(uint64_t paddr) {
  assert(paddr >= 0x80000000);

  return *((uint32_t *)(mem + paddr - 0x80000000));
}

static void load_image(char *image) {
  FILE *fp = fopen(image, "r");
  assert(fp != 0);

  fseek(fp, 0, SEEK_END);
  long size = ftell(fp);

  rewind(fp);

  int ret = fread(mem, size, 1, fp);
  assert(ret == 1);

  fclose(fp);

  printf("The image is %s, size is %ld.\n", image, size);
}

int main(int argc, char *argv[]) {

  if (argc == 2) {
    load_image(argv[1]);
  }

  context = new VerilatedContext;
  vcd = new VerilatedVcdC;
  top = new VTop{context};

  context->commandArgs(argc, argv);

  Verilated::traceEverOn(true);

  top->trace(vcd, 5);
  vcd->open("sim.vcd");

  reset(10);
  while (!context->gotFinish()) {
    context->timeInc(1);
    top->clock ^= 1;
    if (top->clock == 1) {
      top->io_inst = paddr_read(top->io_pc);
    }
    top->eval();
    vcd->dump(context->time());

    if (halt == true) {
      break;
    }
  }

  vcd->close();

  return 0;
}

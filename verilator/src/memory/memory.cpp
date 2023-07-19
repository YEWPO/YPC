// #include "VTop__Dpi.h"
#include <svdpi.h>
#include <verilated_dpi.h>
#include "VTop.h"

#include "common.h"
#include "memory/vmem.h"

#define WRITE 1
#define READ 0

extern VTop *top;
static bool pre_clock;

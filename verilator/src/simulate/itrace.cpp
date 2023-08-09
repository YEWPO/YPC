#include "simulate/simulator.h"

typedef struct Iring {
  char log[128];
} Iring;

#define IRING_SIZE 36
#define IRING_NEXT(p) ((p + 1) % IRING_SIZE)
#define IRING_PREV(p) ((p - 1 + IRING_SIZE) % IRING_SIZE)
static Iring iring[IRING_SIZE] = {};
static int iring_head = 0;

void iring_print() {
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

void add2iring(Decode *_this) {
#ifdef CONFIG_ITRACE_COND
  strcpy(iring[iring_head].log, _this->logbuf);
#endif
  iring_head = IRING_NEXT(iring_head);
}

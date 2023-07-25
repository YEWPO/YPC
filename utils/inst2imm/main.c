#include <stdint.h>
#include <stdio.h>

#define BITMASK(bits) ((1ull << (bits)) - 1)
#define BITS(x, hi, lo) (((x) >> (lo)) & BITMASK((hi) - (lo) + 1)) // similar to x[hi:lo] in verilog
#define SEXT(x, len) ({ struct { int64_t n : len; } __x = { .n = x }; (uint64_t)__x.n; })

#define immI() do { outI = SEXT(BITS(i, 31, 20), 12); } while(0)
#define immU() do { outU = SEXT(BITS(i, 31, 12), 20) << 12; } while(0)
#define immS() do { outS = (SEXT(BITS(i, 31, 25), 7) << 5) | BITS(i, 11, 7); } while(0)
#define immB() do { outB = (SEXT(BITS(i, 31, 31), 1) << 12) | (BITS(i, 7, 7) << 11) \
                            | (BITS(i, 30, 25) << 5) | (BITS(i, 11, 8) << 1); } while(0)
#define immJ() do { outJ = (SEXT(BITS(i, 31, 31), 1) << 20) | (BITS(i, 19, 12) << 12) \
                            | (BITS(i, 20, 20) << 11) | (BITS(i, 30, 21) << 1); } while(0)

uint32_t i;
uint64_t outI, outU, outS, outB, outJ;

int main() {
  printf("Enter inst(Hex): ");

  scanf("%x", &i);
  immI();
  immU();
  immS();
  immB();
  immJ();

  printf("I-type: 0x%lx %ld\n", outI, outI);
  printf("U-type: 0x%lx %ld\n", outU, outU);
  printf("S-type: 0x%lx %ld\n", outS, outS);
  printf("B-type: 0x%lx %ld\n", outB, outB);
  printf("J-type: 0x%lx %ld\n", outJ, outJ);

  return 0;
}

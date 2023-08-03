#include "memory/host.h"
#include "memory/pmem.h"

static uint8_t PG_ALIGN pmem[CONFIG_MSIZE];
static uint8_t local_img[] = {
  0x97, 0x02, 0x00, 0x00, // auipc t0,0
  0x23, 0xb8, 0x02, 0x00, // sd  zero,16(t0)
  0x03, 0xb5, 0x02, 0x01, // ld  a0,16(t0)
  0x73, 0x00, 0x10, 0x00, // ebreak (used as npc_trap)
  0xef, 0xbe, 0xad, 0xde, // some data
};

uint8_t* guest_to_host(paddr_t paddr) { return pmem + paddr - CONFIG_MBASE; }
paddr_t host_to_guest(uint8_t *haddr) { return haddr - pmem + CONFIG_MBASE; }

static word_t pmem_read(paddr_t addr, int len) {
  word_t data = host_read(guest_to_host(addr), len);
#ifdef CONFIG_MTRACE_COND
  Log("read: 0x%016lx 0x%016lx %d", addr, data, len);
  log_write("read: 0x%016lx 0x%016lx %d\n", addr, data, len);
#endif
  return data;
}

static void pmem_write(paddr_t addr, int len, word_t data) {
#ifdef CONFIG_MTRACE_COND
  Log("write: 0x%016lx 0x%016lx %d", addr, data, len);
  log_write("write: 0x%016lx 0x%016lx %d\n", addr, data, len);
#endif
  host_write(guest_to_host(addr), len, data);
}

void init_mem() {
#if   defined(CONFIG_PMEM_MALLOC)
  pmem = malloc(CONFIG_MSIZE);
  assert(pmem);
#endif
#ifdef CONFIG_MEM_RANDOM
  uint32_t *p = (uint32_t *)pmem;
  int i;
  for (i = 0; i < (int) (CONFIG_MSIZE / sizeof(p[0])); i ++) {
    p[i] = rand();
  }
#endif
  Log("physical memory area [" FMT_PADDR ", " FMT_PADDR "]", PMEM_LEFT, PMEM_RIGHT);

  memcpy(guest_to_host(RESET_VECTOR), local_img, sizeof(local_img));
}

word_t paddr_read(paddr_t addr, int len) {
  if (likely(in_pmem(addr))) {
    return pmem_read(addr, len);
  }
  Assert(0, "address = " FMT_PADDR " is out of bound", addr);
  return 0;
}

void paddr_write(paddr_t addr, int len, word_t data) {
  if (likely(in_pmem(addr))) {
    pmem_write(addr, len, data);
    return;
  }
  Assert(0, "address = " FMT_PADDR " is out of bound", addr);
}

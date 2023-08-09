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

word_t pmem_read(paddr_t addr) {
 paddr_t r_addr = addr & ADDR_MASK;
#ifdef CONFIG_MTRACE_COND
  Log("read from address: " FMT_PADDR, r_addr);
  log_write("read from address: " FMT_PADDR "\n", r_addr);
#endif
  word_t data = host_read(guest_to_host(r_addr), 8);
#ifdef CONFIG_MTRACE_COND
  Log("read data: " FMT_WORD, data);
  log_write("read data: " FMT_WORD "\n", data);
#endif
  return data;
}

void pmem_write(paddr_t addr, word_t data, char mask) {
  paddr_t w_addr = addr & ADDR_MASK;
#ifdef CONFIG_MTRACE_COND
  Log("write to address: " FMT_PADDR " and data: " FMT_WORD, w_addr, data);
  log_write("write to address: " FMT_PADDR " and data: " FMT_WORD "\n", w_addr, data);
  Log("mask is %d", mask);
  log_write("mask is %d\n", mask);
#endif
  bool pre_mask = false;
  int w_len = 0;
  for (int i = 0; i < 8; ++i) {
    if (mask & (1 << i)) {
      if (pre_mask) {
        w_len++;
      } else {
        w_len = 1;
        pre_mask = true;
      }
    } else {
      if (pre_mask) {
        host_write(guest_to_host(w_addr + i - w_len), w_len, data);
        data >>= (8 * w_len);
        pre_mask = false;
        w_len = 0;
      }
      data >>= 8;
    }
  }
  // write high bits
  if (w_len) {
    host_write(guest_to_host(w_addr + 8 - w_len), w_len, data);
  }
#ifdef CONFIG_MTRACE_COND
  Log("write finish");
  log_write("write finish\n");
#endif
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

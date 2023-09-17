#include "device/map.h"

#ifdef CONFIG_HAS_SERIAL

#define CH_OFFSET 0

static uint8_t *serial_base = NULL;

static void serial_putc(char ch) {
  MUXDEF(CONFIG_TARGET_AM, putch(ch), putc(ch, stderr));
}

static void serial_io_handler(uint32_t offset, int len, bool is_write) {
  if (len != 1) {
#ifdef CONFIG_MTRACE_COND
    Log("serial len is not 1!");
#endif
    return;
  }
  switch (offset) {
    /* We bind the serial port with the host stderr in NEMU. */
    case CH_OFFSET:
      if (is_write) serial_putc(serial_base[0]);
      else {
#ifdef CONFIG_MTRACE_COND
        Log("do not support read");
#endif
        return;
      }
      break;
    default: Assert(0, "do not support offset = %d", offset);
  }
}

void init_serial() {
  serial_base = new_space(8);
  add_mmio_map("serial", CONFIG_SERIAL_MMIO, serial_base, 8, serial_io_handler);
}

#endif

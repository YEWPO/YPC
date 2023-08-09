#include "device/map.h"

void init_serial();
void init_timer();

void init_device() {
  init_map();

  IFDEF(CONFIG_HAS_SERIAL, init_serial());
  IFDEF(CONFIG_HAS_TIMER, init_timer());
}

#include "device/device.h"
#include "device/map.h"

#ifdef CONFIG_HAS_TIMER
#include "device/rtc.h"
#endif
#ifdef CONFIG_HAS_SERIAL
#include "device/serial.h"
#endif

void init_device() {
  init_map();

  IFDEF(CONFIG_HAS_TIMER, init_rtc());
  IFDEF(CONFIG_HAS_SERIAL, init_serial());
}

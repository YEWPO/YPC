#include "device/device.h"
#include "device/map.h"
#include <SDL2/SDL.h>
#include "utils/timer.h"
#include "utils/cpu.h"

#ifdef CONFIG_HAS_TIMER
#include "device/rtc.h"
#endif
#ifdef CONFIG_HAS_SERIAL
#include "device/serial.h"
#endif
#ifdef CONFIG_HAS_KEYBOARD
#include "device/keyboard.h"
#endif

extern NPCState npc_state;

#define TIMER_HZ 60

void device_update() {
  static uint64_t last = 0;
  uint64_t now = get_time();
  if (now - last < 1000000 / TIMER_HZ) {
    return;
  }
  last = now;

  // IFDEF(CONFIG_HAS_VGA, vga_update_screen());

  SDL_Event event;
  while (SDL_PollEvent(&event)) {
    switch (event.type) {
      case SDL_QUIT:
        npc_state.state = NPC_QUIT;
        break;
#ifdef CONFIG_HAS_KEYBOARD
      // If a key was pressed
      case SDL_KEYDOWN:
      case SDL_KEYUP: {
        uint8_t k = event.key.keysym.scancode;
        bool is_keydown = (event.key.type == SDL_KEYDOWN);
        send_key(k, is_keydown);
        break;
      }
#endif
      default: break;
    }
  }
}

void sdl_clear_event_queue() {
  SDL_Event event;
  while (SDL_PollEvent(&event));
}

void init_device() {
  init_map();

  IFDEF(CONFIG_HAS_TIMER, init_rtc());
  IFDEF(CONFIG_HAS_SERIAL, init_serial());
  IFDEF(CONFIG_HAS_KEYBOARD, init_i8042());
}

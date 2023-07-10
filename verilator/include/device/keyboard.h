#ifndef _KEYBOARD_H
#define _KEYBOARD_H

#include "common.h"

void init_i8042();
void send_key(uint8_t scancode, bool is_keydown);

#endif

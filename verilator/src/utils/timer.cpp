#include "common.h"
#include <sys/time.h>

static uint64_t boot_time;

static uint64_t get_time_internal() {
  struct timeval now;
  gettimeofday(&now, NULL);
  uint64_t us = now.tv_sec * 1e6 + now.tv_usec;
  return us;
}

uint64_t get_time() {
  if (boot_time == 0) {
    boot_time = get_time_internal();
  }
  uint64_t now = get_time_internal();
  return now - boot_time;
}

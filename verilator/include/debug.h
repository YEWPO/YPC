#ifndef _DEBUG_H
#define _DEBUG_H

#include "macro.h"

#define Log(format, ...) \
  printf(ANSI_FMT("[%s:%d %s] " format, ANSI_FG_BLUE) "\n", \
    __FILE__, __LINE__, __func__, ## __VA_ARGS__)

#define Assert(cond, format, ...) do { \
  if (!(cond)) { \
    printf(ANSI_FMT("Failed on [%s:%d %s], reason: " format, ANSI_FG_RED) "\n", \
    __FILE__, __LINE__, __func__, ## __VA_ARGS__); \
    extern void assert_fail_msg(); \
    assert_fail_msg(); \
    assert(cond); \
  } \
} while (0)

#endif

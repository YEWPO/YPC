#ifndef _COMMON_H
#define _COMMON_H

#include <cstdint>
#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <cassert>
#include <inttypes.h>

#include "debug.h"
#include "utils/log.h"
#include "generated/autoconf.h"

typedef uint64_t word_t;
typedef int64_t sword_t;
#define FMT_WORD "0x%016" PRIx64

typedef word_t vaddr_t;
typedef uint64_t paddr_t;
#define FMT_PADDR "0x%016" PRIx64
typedef uint16_t ioaddr_t;

#endif

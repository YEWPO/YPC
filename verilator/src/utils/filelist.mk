CFLAGS += -fPIE -I/usr/lib/llvm-14/include -D_GNU_SOURCE -D__STDC_CONSTANT_MACROS -D__STDC_LIMIT_MACROS
LIBS += $(shell llvm-config --libs)

WORK_DIR = $(abspath .)
BUILD_DIR = $(NPC_HOME)/build
VERILATOR_BUILD_DIR = $(BUILD_DIR)/verilator_dir
VERILOG_BUILD_DIR = $(BUILD_DIR)/verilog_dir

TOPNAME = Top

VERILATOR = verilator
GDB = gdb

VERILATOR_FLAGS = --cc --build -j 8 \
									--trace

BIN = $(BUILD_DIR)/$(TOPNAME)
IMG ?=
override ARGS ?= --log=$(BUILD_DIR)/npc.log

VSRCS = $(shell find $(VERILOG_BUILD_DIR) -name "*.v")
CSRCS = $(shell find $(WORK_DIR) -name "*.cpp")

INCLUDE_PATHS ?= $(WORK_DIR)/include
INCFLAGS = $(addprefix -I, $(INCLUDE_PATHS))
CFLAGS += $(INCFLAGS)
CFLAGS += -g3

LDFLAGS += -lreadline

include $(WORK_DIR)/script/config.mk
include $(WORK_DIR)/script/native.mk

.PHONY: run

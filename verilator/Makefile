WORK_DIR = $(abspath .)
BUILD_DIR = $(NPC_HOME)/build
VERILATOR_BUILD_DIR = $(BUILD_DIR)/verilator_dir
VERILOG_BUILD_DIR = $(BUILD_DIR)/verilog_dir

TOPNAME = Top

VERILATOR = verilator

VERILATOR_FLAGS = --cc --build -j 8 \
									--trace

BIN = $(BUILD_DIR)/$(TOPNAME)
IMG ?=

VSRCS = $(shell find $(VERILOG_BUILD_DIR) -name "*.v")
CSRCS = $(shell find $(WORK_DIR) -name "*.cpp")

INCLUDE_PATHS ?= $(WORK_DIR)/include
INCFLAGS = $(addprefix -I, $(INCLUDE_PATHS))
CFLAGS += $(INCFLAGS)
CFLAGS += -g3

$(BIN): $(CSRCS) $(VSRCS)
	$(VERILATOR) $(VERILATOR_FLAGS) \
		--top-module $(TOPNAME) $^ \
		$(addprefix -CFLAGS , $(CFLAGS)) \
		--Mdir $(VERILATOR_BUILD_DIR) -exe -o $(BIN)

run: $(BIN)
	@echo simulating...
	$^ $(IMG)

.PHONY: run

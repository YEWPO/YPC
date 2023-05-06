WORK_DIR = $(abspath .)
BUILD_DIR = $(WORK_DIR)/build

export PATH := $(PATH):$(abspath ./utils)

test:
	mill -i __.test

VERILOG_DIR = $(BUILD_DIR)/verilog_dir

verilog:
	$(call git_commit, "generate verilog")
	@rm -rf $(BUILD_DIR)
	@mkdir -p $(BUILD_DIR)
	mill -i __.test.runMain Elaborate -td $(VERILOG_DIR)

help:
	mill -i __.test.runMain Elaborate --help

compile:
	mill -i __.compile

bsp:
	mill -i mill.bsp.BSP/install

reformat:
	mill -i __.reformat

checkformat:
	mill -i __.checkFormat

# ============== verilator =============
TOPNAME = Top

VERILATOR = verilator
GTKWAVE = gtkwave

VERILATOR_FLAGS = --cc --build -j 8 \
									--trace \
									-I./build/verilog_dir

VERILATOR_DIR = $(BUILD_DIR)/verilator_dir
BIN = $(BUILD_DIR)/$(TOPNAME)
IMG ?=

TOPV = $(VERILOG_DIR)/Top.v
MAINC = $(WORK_DIR)/verilator/main.cpp

$(TOPV): verilog

$(BIN): $(MAINC) $(TOPV)
	$(VERILATOR) $(VERILATOR_FLAGS) \
		--top-module $(TOPNAME) $^ \
		--Mdir $(VERILATOR_DIR) -exe -o $(BIN) 

sim: $(BIN)
	$(call git_commit, "sim RTL") # DO NOT REMOVE THIS LINE!!!
	@echo "simulating..."
	@$^ $(IMG)

VCD_FILE = $(WORK_DIR)/sim.vcd

wv:
	$(GTKWAVE) $(VCD_FILE)

clean:
	-rm -rf $(BUILD_DIR) $(VCD_FILE)

include ../Makefile

.PHONY: test verilog help compile bsp reformat checkformat clean sim wv

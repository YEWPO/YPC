WORK_DIR = $(abspath .)
BUILD_DIR = $(WORK_DIR)/build
VERILATOR_DIR = $(WORK_DIR)/verilator

export PATH := $(PATH):$(abspath ./utils)

test:
	mill -i __.test

VERILOG_BUILD_DIR = $(BUILD_DIR)/verilog_dir

verilog:
	$(call git_commit, "generate verilog")
	@rm -rf $(BUILD_DIR)
	@mkdir -p $(BUILD_DIR)
	mill -i __.test.runMain Elaborate -td $(VERILOG_BUILD_DIR)

help:
	mill -i __.test.runMain Elaborate --help

compile:
	mill -i __.compile

bsp:
	mill -i mill.bsp.BSP/install --jobs 8

reformat:
	mill -i __.reformat

checkformat:
	mill -i __.checkFormat

IMG ?=
ARGS ?=

sim: verilog
	$(call git_commit, "sim RTL")
	$(MAKE) -C $(VERILATOR_DIR) run

gdb: verilog
	$(MAKE) -C $(VERILATOR_DIR) ARGS="$(ARGS)" IMG=$(IMG) gdb

GTKWAVE = gtkwave
VCD_FILE = $(BUILD_DIR)/sim.vcd

wv:
	$(GTKWAVE) $(VCD_FILE)

PYTHON = python3
GEN_INST_SCRIPT = $(WORK_DIR)/utils/decodegen/main.py

gendecode:
	$(PYTHON) $(GEN_INST_SCRIPT)

clean:
	-rm -rf $(BUILD_DIR) test_run_dir
	-mill clean

include ../Makefile

.PHONY: test verilog help compile bsp reformat checkformat clean sim wv

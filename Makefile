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
	mill -i mill.bsp.BSP/install

reformat:
	mill -i __.reformat

checkformat:
	mill -i __.checkFormat

IMG ?=

sim: verilog
	$(call git_commit, "sim RTL")
	$(MAKE) -C $(VERILATOR_DIR) IMG=$(IMG) run

GTKWAVE = gtkwave
VCD_FILE = $(BUILD_DIR)/sim.vcd

wv:
	$(GTKWAVE) $(VCD_FILE)

clean:
	-rm -rf $(BUILD_DIR)

include ../Makefile

.PHONY: test verilog help compile bsp reformat checkformat clean sim wv

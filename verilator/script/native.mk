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
INCLUDE_PATHS += $(VERILATOR_BUILD_DIR)
INCLUDE_PATHS += /usr/local/share/verilator/include/vltstd
INCLUDE_PATHS += /usr/local/share/verilator/include
INCFLAGS = $(addprefix -I, $(INCLUDE_PATHS))
CFLAGS += $(INCFLAGS)
CFLAGS += -g3

LDFLAGS += -lreadline $(LIBS) -ldl

$(BIN): $(VSRCS) $(CSRCS)
	$(VERILATOR) $(VERILATOR_FLAGS) \
		--top-module $(TOPNAME) $^ \
		$(addprefix -CFLAGS , $(CFLAGS)) \
		$(addprefix -LDFLAGS , $(LDFLAGS)) \
		--Mdir $(VERILATOR_BUILD_DIR) -exe -o $@

run: $(BIN)
	@echo running...
	$^ $(ARGS) $(IMG)

gdb: $(BIN)
	$(GDB) -ex 'set args $(ARGS) $(IMG)' $^

.PHONY: run gdb

$(BIN): $(CSRCS) $(VSRCS)
	$(VERILATOR) $(VERILATOR_FLAGS) \
		--top-module $(TOPNAME) $^ \
		$(addprefix -CFLAGS , $(CFLAGS)) \
		$(addprefix -LDFLAGS , $(LDFLAGS)) \
		--Mdir $(VERILATOR_BUILD_DIR) -exe -o $(BIN)

run: $(BIN)
	@echo running...
	$^ $(ARGS) $(IMG)

gdb: $(BIN)
	$(GDB) -ex 'set args $(ARGS) $(IMG)' $^


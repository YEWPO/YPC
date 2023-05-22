module Memory(
  input [63:0] addr,
  input [63:0] w_data,
  input op,
  input [3:0] len,
  output [63:0] r_data
);

  import "DPI-C" function word_t memory_io(vaddr_t addr, word_t w_data, bool op, int len);

  assign r_data = memory_io(addr, w_data, op, len);

endmodule

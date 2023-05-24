module Memory(
  input [63:0] addr,
  input [63:0] w_data,
  input op,
  input [31:0] len,
  output [63:0] r_data
);

  import "DPI-C" function longint memory_io(input longint addr, input longint w_data, input op, input int len);

  assign r_data = memory_io(addr, w_data, op, len);

endmodule

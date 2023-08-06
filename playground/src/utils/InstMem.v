module InstMem (
  input [63:0] addr,
  input en,
  output [63:0] r_data
);

  import "DPI-C" function void nmem_read(input longint addr, output longint r_data, input logic en);

  always @(*) begin
    nmem_read(addr, r_data, en);
  end

endmodule

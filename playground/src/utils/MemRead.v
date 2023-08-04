module MemRead(
  input r_en,
  input [63:0] addr
  output [63:0] r_data
);

  import "DPI-C" function void nmem_read(input longint addr, output longint r_data, input logic r_en);

  always @(*) begin
    nmem_read(addr, r_data, r_en);
  end

endmodule

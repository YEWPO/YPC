module MemWrite(
  input w_en,
  input [63:0] addr,
  input [63:0] w_data,
  input [63:0] mask
);

  import "DPI-C" function nmem_write(input longint addr, input longint w_data, input longint mask, input logic w_en);

  always @(*) begin
    nmem_write(addr, w_data, mask, w_en);
  end

endmodule

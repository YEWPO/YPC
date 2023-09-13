module MemWrite (
  input [63:0] addr,
  input [63:0] w_data,
  input [7:0] mask
);

  import "DPI-C" function void nmem_write(input longint addr, input longint w_data, input byte mask);

  always @(*) begin
    nmem_write(addr, w_data, mask);
  end

endmodule

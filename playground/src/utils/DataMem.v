module DataMem (
  input [63:0] addr,
  input [63:0] w_data,
  input [63:0] mask,
  output [63:0] r_data
);

  import "DPI-C" function void nmem_read(input longint addr, output longint r_data, input longint mask);
  import "DPI-C" function void nmem_write(input longint addr, input longint w_data, input longint mask);

  always @(*) begin
    nmem_read(addr, r_data, mask);
    nmem_write(addr, w_data, mask);
  end

endmodule

module MemRead (
  input [63:0] addr,
  output [63:0] r_data
);

  import "DPI-C" function void nmem_read(input longint addr, output longint r_data);

  always @(*) begin
    nmem_read(addr, r_data);
  end

endmodule

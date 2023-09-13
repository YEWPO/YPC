module InstMem (
  input [63:0] addr,
  output [63:0] r_data
);

  import "DPI-C" function void nmem_ifetch(input longint addr, output longint r_data);

  always @(*) begin
    nmem_ifetch(addr, r_data);
  end

endmodule

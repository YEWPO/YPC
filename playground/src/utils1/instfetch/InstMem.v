module InstMem (
  input [63:0] addr,
  input en,
  output [63:0] r_data
);

  import "DPI-C" function void nmem_ifetch(input longint addr, output longint r_data, input logic en);

  always @(*) begin
    nmem_ifetch(addr, r_data, en);
  end

endmodule

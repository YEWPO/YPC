module InstMem (
  input [63:0] addr,
  output [31:0] inst,
  input en
);

  import "DPI-C" function void nmem_ifetch(input longint addr, output int inst, input logic en);

  always @(*) begin
    nmem_ifetch(addr, inst, en);
  end

endmodule

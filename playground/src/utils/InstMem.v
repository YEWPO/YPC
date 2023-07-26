module InstMem (
  input clock,
  input [63:0] addr,
  output [31:0] inst
);

  import "DPI-C" function void npc_mem_ifetch(input longint addr, output int inst);

  always @(posedge clock) begin
    npc_mem_ifetch(addr, inst);
  end

endmodule

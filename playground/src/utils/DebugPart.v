module DebugPart (
  input [63:0] pc,
  input [31:0] inst,
  input [63:0] dnpc
);

  import "DPI-C" function void inst_finish(input longint pc, input int inst, input longint dnpc);

  always @(*) begin
    inst_finish(pc, inst, dnpc);
  end

endmodule

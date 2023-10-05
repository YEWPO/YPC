module DebugInfo (
  input [63:0] pc,
  input [31:0] inst,
  input [63:0] dnpc,
  input device_op
);

  import "DPI-C" function void inst_finish(input longint pc, input int inst, input longint dnpc, input logic device_op);

  always @(*) begin
    inst_finish(pc, inst, dnpc, device_op);
  end

endmodule

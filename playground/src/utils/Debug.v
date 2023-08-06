module Debug (
  input [63:0] pc,
  input [31:0] inst
);

  import "DPI-C" function void inst_finish(input longint pc, input int inst);

  always @(*) begin
    inst_finish(pc, inst);
  end

endmodule

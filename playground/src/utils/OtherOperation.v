module OtherOperation (
  input ebreak_op,
  input invalid_op,
  input [63:0] pc
);

  import "DPI-C" function void ebreak(input longint pc);
  import "DPI-C" function void invalid(input longint pc);

  always @(*) begin
    if (ebreak_op) begin
      ebreak(pc);
    end
    if (invalid_op) begin
      invalid(pc);
    end
  end

endmodule

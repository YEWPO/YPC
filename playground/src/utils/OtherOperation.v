module OtherOperation (
  input ebreak_op,
  input invalid_op
);

  import "DPI-C" function void ebreak();
  import "DPI-C" function void invalid();

  always @(*) begin
    if (ebreak_op) begin
      ebreak();
    end
    if (invalid_op) begin
      invalid();
    end
  end

endmodule

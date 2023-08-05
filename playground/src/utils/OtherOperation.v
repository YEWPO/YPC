module OtherOperation (
  input ebreak,
  input invalid
);

  import "DPI-C" function void ebreak();
  import "DPI-C" funtion void invalid();

  always @(*) begin
    if (ebreak) begin
      ebreak();
    end
    if (invalid) begin
      invalid();
    end
  end

endmodule

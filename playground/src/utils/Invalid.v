module Invalid (
  input in
);

  import "DPI-C" function void invalid();

  always @(*) begin
    if (in) begin
      invalid();
    end
  end

endmodule

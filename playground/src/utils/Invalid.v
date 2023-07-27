module Invalid (
  input clock,
  input in
);

  import "DPI-C" function void invalid();

  always @(posedge clock) begin
    if (in) begin
      invalid();
    end
  end

endmodule

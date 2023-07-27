module Ebreak (
  input clock,
  input in
);

  import "DPI-C" function void ebreak();

  always @(posedge clock) begin
    if (in) begin
      ebreak();
    end
  end

endmodule

module Ebreak (
  input in
);

  import "DPI-C" function void ebreak();

  always @(*) begin
    if (in) begin
      ebreak();
    end
  end

endmodule

module Ebreak (
  input [31:0] inst
);

  import "DPI-C" function void ebreak();

  always @(*) begin
    if (inst == 32'h00100073) begin
      ebreak();
    end
  end

endmodule

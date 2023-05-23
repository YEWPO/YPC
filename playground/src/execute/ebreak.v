module ebreak (
  input [2:0] op_type
);

  import "DPI-C" function void ebreak();

  always @(*) begin
    if (op_type == 3'b110) begin
      ebreak();
    end
  end

endmodule

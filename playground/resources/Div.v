module Div(
  input [63:0] src1,
  input [63:0] src2,
  output [31:0] res_32,
  output [63:0] res_64
);

  assign res_32 = $signed(src1[31:0]) / $signed(src2[31:0]);
  assign res_64 = $signed(src1) / $signed(src2);

endmodule

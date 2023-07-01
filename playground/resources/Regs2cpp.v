module Regs2cpp(
  input [31:0] regs[63:0]
);

  import "DPI-C" function void set_gpr_ptr(input logic [63:0] regs[]);

  initial set_gpr_ptr(regs);

endmodule

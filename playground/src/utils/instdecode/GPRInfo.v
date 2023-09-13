module GPRInfo (
  input [64*32-1:0] inbits
);

  logic [63:0] regs[31:0];

  import "DPI-C" function void set_gpr_ptr(input logic [63:0] regs[]);

  always @(*) begin
    for (int i = 0; i < 32; ++i) begin
      assign regs[i] = inbits[i*64 +: 64];
    end
    set_gpr_ptr(regs);
  end

endmodule

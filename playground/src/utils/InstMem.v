module InstMem (
  input [63:0] addr,
  input en,
  output [31:0] inst
);

  import "DPI-C" function void nmem_read(input longint addr, output int inst, input logic en);

  wire [63:0] r_data;

  always @(*) begin
    nmem_read(addr, inst, en);
    inst <= addr[2] == 1'b0 ? r_data[31:0] : r_data[63:32];
  end

endmodule

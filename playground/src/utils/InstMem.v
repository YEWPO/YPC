module InstMem (
  input [63:0] addr,
  output [31:0] inst,
  input en
);

  import "DPI-C" function void nmem_read(input longint addr, output longint r_data, input logic en);

  wire [63:0] r_data;

  always @(*) begin
    nmem_read(addr, r_data, en);
    if (en) begin
      inst <= addr[2] == 1'b1 ? r_data[63:32] : r_data[31:0];
    end
    else begin
      inst <= 32'h13
    end
  end

endmodule

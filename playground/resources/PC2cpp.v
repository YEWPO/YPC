module PC2cpp (
  input [63:0] pc_val
);

  import "DPI-C" function void set_pc_val(input logic [63:0] pc_val);

  always @(*) begin
    set_pc_val(pc_val);
  end

endmodule

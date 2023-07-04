module Pmem(
  input mem_en,
  input w_en,
  input signed_en,
  input [63:0] addr,
  input [63:0] w_data,
  input [63:0] r_mask,
  output [63:0] r_data
);

  import "DPI-C" function void mem(input mem_en, input w_en, input signed_en,
                                    input longint addr, input longint w_data,
                                  input longint r_mask, output longint r_data);

  always @(*) begin
    mem(mem_en, w_en, signed_en, addr, w_data, r_mask, r_data);
  end

endmodule

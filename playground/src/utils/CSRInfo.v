module CSRInfo (
  input [63:0] mstatus,
  input [63:0] mtvec,
  input [63:0] mepc,
  input [63:0] mcause
);

  import "DPI-C" function void set_csr(input longint mstatus, input longint mtvec, input longint mepc, input longint mcause);

  always @(*) begin
    set_csr(mstatus, mtvec, mepc, mcause);
  end

endmodule

package execute

import chisel3._

class SelectorB extends Module {
  val io = IO(new Bundle {
    val imm_val = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))

    val sel_sig_imm = Input(Bool())

    val sel_out = Output(UInt(64.W))
  })

  io.sel_out := Mux(io.sel_sig_imm, io.imm_val, io.src2)
}

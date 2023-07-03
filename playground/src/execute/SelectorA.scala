package execute

import chisel3._

class SelectorA extends Module {
  val io = IO(new Bundle {
    val pc_val = Input(UInt(64.W))
    val src1 = Input(UInt(64.W))

    val sel_sig_0 = Input(Bool())
    val sel_sig_pc = Input(Bool())

    val sel_out = Output(UInt(64.W))
  })

  io.sel_out := Mux(
    io.sel_sig_0,
    0.U(64.W),
    Mux(io.sel_sig_pc, io.pc_val, io.src1)
    )
}

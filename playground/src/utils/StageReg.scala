package utils

import chisel3._

class StageRegIO[+T <: Data](bundle: T) extends Bundle {
  val in     = Input(bundle)
  val out    = Output(bundle)
  val enable = Input(Bool())
  val reset  = Input(Bool())
}

class StageReg[+T <: Data](bundle: T) extends Module {
  val io = IO(new StageRegIO[T](bundle))

  /* ========== register ========== */
  val reg = RegInit(0.U.asTypeOf(bundle))

  /* ========== sequential circuit ========== */
  when(io.enable) {
    // stall current stage
    reg := io.in
  }
  when(io.reset) {
    // reset current stage
    reg := 0.U.asTypeOf(bundle)
  }

  /* ========== combinational circuit ========== */
  io.out := reg
}

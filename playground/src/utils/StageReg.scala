package utils

import chisel3._
import bundles._

class StageRegIO[+T <: Data](bundle: T) extends Bundle {
  val in      = Input(bundle)
  val out     = Output(bundle)
  val control = Input(new StageControlBundle)
}

class StageReg[+T <: Data](bundle: T) extends Module {
  val io = IO(new StageRegIO[T](bundle))

  /* ========== Register ========== */
  val reg = RegInit(0.U.asTypeOf(bundle))

  /* ========== Sequential Circuit ========== */
  when(io.control.enable) {
    // stall current stage
    reg := io.in
  }
  when(io.control.reset) {
    // reset current stage
    reg := 0.U.asTypeOf(bundle)
  }

  /* ========== Combinational Circuit ========== */
  io.out := reg
}

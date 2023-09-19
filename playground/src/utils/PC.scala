package utils

import chisel3._
import bundles._
import chisel3.experimental.BundleLiterals._
import macros._

class PC extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new StageRegIO(UInt(64.W)))

  /* ========== Register ========== */
  val reg = Reg(UInt(64.W))

  /* ========== Sequential Circuit ========== */
  when(io.control.enable) {
    reg := io.in
  }
  when(io.control.reset) {
    reg := CommonMacros.PC_RESET_VAL
  }

  /* ========== Combination Circuit ========== */
  io.out := reg
}

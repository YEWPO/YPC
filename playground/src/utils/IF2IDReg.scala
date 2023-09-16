package utils

import chisel3._
import chisel3.experimental.BundleLiterals._
import bundles._
import macros._

class IF2IDReg extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new StageRegIO(new IF2IDBundle))

  /* ========== Register ========== */
  val reg = Reg(new IF2IDBundle)

  /* ========== Sequential Circuit ========== */
  when(io.control.enable) {
    reg := io.in
  }
  when(io.control.reset) {
    reg := (new IF2IDBundle).Lit(
      _.data.pc   -> CommonMacros.PC_RESET_VAL,
      _.data.snpc -> CommonMacros.PC_RESET_VAL,
      _.data.inst -> CommonMacros.INST_RESET_VAL
    )
  }

  /* ========== Combination Circuit ========== */
  io.out := reg
}

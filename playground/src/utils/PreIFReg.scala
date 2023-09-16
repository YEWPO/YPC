package utils

import chisel3._
import bundles._
import chisel3.experimental.BundleLiterals._
import macros._

class PreIFReg extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new StageRegIO(new PreIFBundle))

  /* ========== Register ========== */
  val reg = Reg(new PreIFBundle)

  /* ========== Sequential Circuit ========== */
  when(io.control.enable) {
    reg := io.in
  }
  when(io.control.reset) {
    reg := (new PreIFBundle).Lit(
      _.data.pc -> CommonMacros.PC_RESET_VAL
    )
  }

  /* ========== Combination Circuit ========== */
  io.out := reg
}

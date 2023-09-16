package utils

import chisel3._
import bundles._
import chisel3.experimental.BundleLiterals._
import macros._

class LS2WBReg extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new StageRegIO(new LS2WBBundle))

  /* ========== Register ========== */
  val reg = Reg(new LS2WBBundle)

  /* ========== Sequential Circuit ========== */
  when(io.control.enable) {
    reg := io.in
  }
  when(io.control.reset) {
    reg := (new LS2WBBundle).Lit(
      _.data -> (new LS2WBDataBundle).Lit(
        _.snpc       -> CommonMacros.PC_RESET_VAL,
        _.pc         -> CommonMacros.PC_RESET_VAL,
        _.dnpc       -> CommonMacros.PC_RESET_VAL,
        _.inst       -> CommonMacros.INST_RESET_VAL,
        _.rd         -> 0.U,
        _.mem_out    -> 0.U,
        _.exe_out    -> 0.U,
        _.csr_w_data -> 0.U,
        _.csr_w_addr -> 0.U
      ),
      _.control -> (new LS2WBControlBundle).Lit(
        _.wb_ctl     -> ControlMacros.WB_CTL_DEFAULT,
        _.reg_w_en   -> ControlMacros.REG_W_DISABLE,
        _.ebreak_op  -> ControlMacros.EBREAK_OP_NO,
        _.invalid_op -> ControlMacros.INVALID_OP_NO,
        _.csr_w_en   -> false.B
      )
    )
  }

  /* ========== Combination Circuit ========== */
  io.out := io.in
}

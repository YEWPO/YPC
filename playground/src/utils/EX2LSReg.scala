package utils

import chisel3._
import bundles._
import chisel3.experimental.BundleLiterals._
import macros._

class EX2LSReg extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new StageRegIO(new EX2LSBundle))

  /* ========== Register ========== */
  val reg = Reg(new EX2LSBundle)

  /* ========== Sequential Circuit ========== */
  when(io.control.enable) {
    reg := io.in
  }
  when(io.control.reset) {
    reg := (new EX2LSBundle).Lit(
      _.data -> (new EX2LSDataBundle).Lit(
        _.pc         -> CommonMacros.PC_RESET_VAL,
        _.snpc       -> CommonMacros.PC_RESET_VAL,
        _.dnpc       -> CommonMacros.PC_RESET_VAL,
        _.inst       -> CommonMacros.INST_RESET_VAL,
        _.rd         -> 0.U,
        _.src2       -> 0.U,
        _.exe_out    -> 0.U,
        _.csr_w_data -> 0.U,
        _.csr_w_addr -> 0.U
      ),
      _.control -> (new EX2LSControlBundle).Lit(
        _.mem_ctl    -> ControlMacros.MEM_CTL_DEFAULT,
        _.wb_ctl     -> ControlMacros.WB_CTL_DEFAULT,
        _.reg_w_en   -> ControlMacros.REG_W_DISABLE,
        _.invalid_op -> ControlMacros.INVALID_OP_NO,
        _.ebreak_op  -> ControlMacros.EBREAK_OP_NO,
        _.csr_w_en   -> false.B
      )
    )
  }

  /* ========== Combination Circuit ========== */
  io.out := reg
}

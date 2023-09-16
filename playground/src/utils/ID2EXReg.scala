package utils

import chisel3._
import chisel3.experimental.BundleLiterals._
import bundles._
import macros._

class ID2EXReg extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new StageRegIO(new ID2EXBundle))

  /* ========== Register ========== */
  val reg = Reg(new ID2EXBundle)

  /* ========== Sequential Circuit ========== */
  when(io.control.enable) {
    reg := io.in
  }
  when(io.control.reset) {
    reg := (new ID2EXBundle).Lit(
      _.data -> (new ID2EXDataBundle).Lit(
        _.pc         -> CommonMacros.PC_RESET_VAL,
        _.snpc       -> CommonMacros.PC_RESET_VAL,
        _.inst       -> CommonMacros.INST_RESET_VAL,
        _.rd         -> 0.U,
        _.src1       -> 0.U,
        _.src2       -> 0.U,
        _.imm        -> 0.U,
        _.csr_data   -> 0.U,
        _.csr_uimm   -> 0.U,
        _.csr_w_addr -> 0.U
      ),
      _.control -> (new ID2EXControlBundle).Lit(
        _.a_ctl       -> ControlMacros.A_CTL_DEFAULT,
        _.b_ctl       -> ControlMacros.B_CTL_DEFAULT,
        _.dnpc_ctl    -> ControlMacros.DNPC_CTL_DEFAULT,
        _.alu_ctl     -> ControlMacros.ALU_CTL_DEFAULT,
        _.mul_ctl     -> ControlMacros.MUL_CTL_DEFAULT,
        _.exe_out_ctl -> ControlMacros.EXE_OUT_DEFAULT,
        _.jump_op     -> ControlMacros.JUMP_OP_DEFAULT,
        _.mem_ctl     -> ControlMacros.MEM_CTL_DEFAULT,
        _.wb_ctl      -> ControlMacros.WB_CTL_DEFAULT,
        _.reg_w_en    -> ControlMacros.REG_W_DISABLE,
        _.ebreak_op   -> ControlMacros.EBREAK_OP_NO,
        _.invalid_op  -> ControlMacros.INVALID_OP_NO,
        _.csr_r_en    -> false.B,
        _.csr_w_en    -> false.B,
        _.csr_src_ctl -> false.B,
        _.csr_op_ctl  -> 0.U
      )
    )
  }

  /* ========== Combination Circuit ========== */
  io.out := reg
}

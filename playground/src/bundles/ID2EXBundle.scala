package bundles

import chisel3._
import chisel3.experimental.BundleLiterals._
import macros._

object ID2EXBundle {
  val id2ex_rst_val = (new ID2EXBundle).Lit(
    _.data -> (new ID2EXDataBundle).Lit(
      _.pc         -> CommonMacros.PC_RESET_VAL,
      _.snpc       -> CommonMacros.PC_RESET_VAL,
      _.dnpc       -> CommonMacros.PC_RESET_VAL,
      _.inst       -> CommonMacros.INST_RESET_VAL,
      _.rd         -> 0.U,
      _.src1       -> 0.U,
      _.src2       -> 0.U,
      _.imm        -> 0.U,
      _.csr_data   -> 0.U,
      _.csr_uimm   -> 0.U,
      _.csr_w_addr -> 0.U,
      _.cause      -> CommonMacros.CAUSE_RESET_VAL
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
      _.reg_w_en    -> ControlMacros.REG_W_DISABLE,
      _.ebreak_op   -> ControlMacros.EBREAK_OP_NO,
      _.invalid_op  -> ControlMacros.INVALID_OP_NO,
      _.mret_op     -> ControlMacros.MRET_OP_NO,
      _.csr_r_en    -> false.B,
      _.csr_w_en    -> false.B,
      _.csr_src_ctl -> false.B,
      _.csr_op_ctl  -> 0.U
    )
  )
}

class ID2EXDataBundle extends Bundle {
  val imm  = UInt(64.W)
  val src1 = UInt(64.W)
  val src2 = UInt(64.W)
  val rd   = UInt(5.W)
  val inst = UInt(32.W)
  val pc   = UInt(64.W)
  val dnpc = UInt(64.W)
  val snpc = UInt(64.W)

  val csr_data   = UInt(64.W)
  val csr_w_addr = UInt(12.W)
  val csr_uimm   = UInt(64.W)

  val cause = UInt(64.W)
}

class ID2EXControlBundle extends Bundle {
  val a_ctl       = UInt(2.W)
  val b_ctl       = Bool()
  val dnpc_ctl    = Bool()
  val alu_ctl     = UInt(5.W)
  val mul_ctl     = UInt(4.W)
  val exe_out_ctl = Bool()
  val jump_op     = UInt(2.W)
  val mem_ctl     = UInt(5.W)
  val reg_w_en    = Bool()
  val ebreak_op   = Bool()
  val invalid_op  = Bool()
  val mret_op     = Bool()

  val csr_r_en    = Bool()
  val csr_w_en    = Bool()
  val csr_op_ctl  = UInt(2.W)
  val csr_src_ctl = Bool()
}

class ID2EXBundle extends Bundle {
  val data    = new ID2EXDataBundle
  val control = new ID2EXControlBundle
}

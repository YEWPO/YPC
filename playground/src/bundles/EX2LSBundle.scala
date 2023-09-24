package bundles

import chisel3._
import chisel3.experimental.BundleLiterals._
import macros._

object EX2LSBundle {
  val ex2ls_rst_val = (new EX2LSBundle).Lit(
    _.data -> (new EX2LSDataBundle).Lit(
      _.pc         -> CommonMacros.PC_RESET_VAL,
      _.dnpc       -> CommonMacros.PC_RESET_VAL,
      _.inst       -> CommonMacros.INST_RESET_VAL,
      _.rd         -> 0.U,
      _.src2       -> 0.U,
      _.exu_out    -> 0.U,
      _.csr_w_data -> 0.U,
      _.csr_w_addr -> 0.U
    ),
    _.control -> (new EX2LSControlBundle).Lit(
      _.mem_ctl    -> ControlMacros.MEM_CTL_DEFAULT,
      _.reg_w_en   -> ControlMacros.REG_W_DISABLE,
      _.invalid_op -> ControlMacros.INVALID_OP_NO,
      _.ebreak_op  -> ControlMacros.EBREAK_OP_NO,
      _.csr_w_en   -> false.B
    )
  )
}

class EX2LSDataBundle extends Bundle {
  val exu_out = UInt(64.W)
  val src2    = UInt(64.W)
  val rd      = UInt(5.W)
  val inst    = UInt(32.W)
  val dnpc    = UInt(64.W)
  val pc      = UInt(64.W)

  val csr_w_addr = UInt(12.W)
  val csr_w_data = UInt(64.W)
}

class EX2LSControlBundle extends Bundle {
  val mem_ctl    = UInt(5.W)
  val reg_w_en   = Bool()
  val ebreak_op  = Bool()
  val invalid_op = Bool()

  val csr_w_en = Bool()
}

class EX2LSBundle extends Bundle {
  val data    = new EX2LSDataBundle
  val control = new EX2LSControlBundle
}

package bundles

import chisel3._
import chisel3.experimental.BundleLiterals._
import macros._

object LS2WBBundle {
  val ls2wb_rst_val = (new LS2WBBundle).Lit(
    _.data -> (new LS2WBDataBundle).Lit(
      _.pc         -> CommonMacros.PC_RESET_VAL,
      _.dnpc       -> CommonMacros.PC_RESET_VAL,
      _.inst       -> CommonMacros.INST_RESET_VAL,
      _.rd         -> 0.U,
      _.lsu_out    -> 0.U,
      _.csr_w_data -> 0.U,
      _.csr_w_addr -> 0.U
    ),
    _.control -> (new LS2WBControlBundle).Lit(
      _.reg_w_en   -> ControlMacros.REG_W_DISABLE,
      _.ebreak_op  -> ControlMacros.EBREAK_OP_NO,
      _.invalid_op -> ControlMacros.INVALID_OP_NO,
      _.csr_w_en   -> false.B
    )
  )
}

class LS2WBDataBundle extends Bundle {
  val lsu_out = UInt(64.W)
  val rd      = UInt(5.W)
  val inst    = UInt(32.W)
  val dnpc    = UInt(64.W)
  val pc      = UInt(64.W)

  val csr_w_addr = UInt(12.W)
  val csr_w_data = UInt(64.W)
}

class LS2WBControlBundle extends Bundle {
  val reg_w_en   = Bool()
  val ebreak_op  = Bool()
  val invalid_op = Bool()

  val csr_w_en = Bool()
}

class LS2WBBundle extends Bundle {
  val data    = new LS2WBDataBundle
  val control = new LS2WBControlBundle
}

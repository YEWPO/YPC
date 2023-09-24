package bundles

import chisel3._
import chisel3.experimental.BundleLiterals._
import macros._

object IF2IDBundle {
  val if2id_rst_val = (new IF2IDBundle).Lit(
    _.data -> (new IF2IDDataBundle).Lit(
      _.pc   -> CommonMacros.PC_RESET_VAL,
      _.snpc -> CommonMacros.PC_RESET_VAL,
      _.inst -> CommonMacros.INST_RESET_VAL
    )
  )
}

class IF2IDDataBundle extends Bundle {
  val inst = UInt(32.W)
  val pc   = UInt(64.W)
  val snpc = UInt(64.W)
}

class IF2IDControlBundle extends Bundle {}

class IF2IDBundle extends Bundle {
  val data    = new IF2IDDataBundle
  val control = new IF2IDControlBundle
}

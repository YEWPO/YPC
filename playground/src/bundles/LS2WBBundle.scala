package bundles

import chisel3._

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

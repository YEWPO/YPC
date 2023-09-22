package bundles

import chisel3._

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

package bundles

import chisel3._

class EX2LSDataBundle extends Bundle {
  val exe_out = UInt(64.W)
  val src2    = UInt(64.W)
  val rd      = UInt(5.W)
  val inst    = UInt(32.W)
  val dnpc    = UInt(64.W)
  val pc      = UInt(64.W)
  val snpc    = UInt(64.W)
}

class EX2LSControlBundle extends Bundle {
  val mem_ctl    = UInt(5.W)
  val wb_ctl     = UInt(2.W)
  val reg_w_en   = Bool()
  val ebreak_op  = Bool()
  val invalid_op = Bool()
}

class EX2LSBundle extends Bundle {
  val data    = new EX2LSDataBundle
  val control = new EX2LSControlBundle
}

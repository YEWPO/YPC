package bundles.execute

import chisel3._

class EXHazardDataBundle extends Bundle {
  val wb_ctl   = UInt(2.W)
  val rd       = UInt(5.W)
  val rd_tag   = Bool()
  val jump_sig = Bool()
}

class EXHazardControlBundle extends Bundle {}

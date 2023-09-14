package bundles.instdecode

import chisel3._

class IDHazardDataBundle extends Bundle {
  val rs1 = UInt(5.W)
  val rs2 = UInt(5.W)
  val rd  = UInt(5.W)

  val rs1_tag = Bool()
  val rs2_tag = Bool()
  val rd_tag  = Bool()
}

class IDHazardControlBundle extends Bundle {
  val fa_ctl = UInt(3.W)
  val fb_ctl = UInt(3.W)
}

package bundles.writeback

import chisel3._

class WBHazardDataBundle extends Bundle {
  val wb_ctl = UInt(2.W)
  val rd     = UInt(5.W)
  val rd_tag = Bool()
}

class WBHazardControlBundle extends Bundle {}

class WBHazardBundle extends Bundle {
  val data    = new WBHazardDataBundle
  val control = new WBHazardControlBundle
}

package bundles.loadstore

import chisel3._

class LSHazardDataBundle extends Bundle {
  val wb_ctl = UInt(2.W)
  val rd     = UInt(5.W)
  val rd_tag = Bool()
}

class LSHazardControlBundle extends Bundle {}

package bundles.instdecode

import chisel3._

class GPRForwardInfo extends Bundle {
  val rd_E = UInt(5.W)
  val rd_M = UInt(5.W)
  val rd_W = UInt(5.W)

  val exu_out = UInt(64.W)
  val lsu_out = UInt(64.W)
  val wbu_out = UInt(64.W)
}

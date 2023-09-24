package bundles.instdecode

import chisel3._

class CSRForwardInfo extends Bundle {
  val addr_E = UInt(12.W)
  val addr_M = UInt(12.W)
  val addr_W = UInt(12.W)

  val data_E = UInt(64.W)
  val data_M = UInt(64.W)
  val data_W = UInt(64.W)
}

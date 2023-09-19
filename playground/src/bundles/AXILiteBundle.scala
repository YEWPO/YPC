package bundles

import chisel3._

class AXILiteReadAddrBundle extends Bundle {
  val araddr = UInt(64.W)
  val arprot = UInt(3.W)
}

class AXILiteReadDataBundle extends Bundle {
  val rdata = UInt(64.W)
  val rresp = UInt(2.W)
}

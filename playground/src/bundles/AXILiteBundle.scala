package bundles

import chisel3._

class AXILiteReadAddrBundle extends Bundle {
  val addr = UInt(64.W)
  val prot = UInt(3.W)
}

class AXILiteReadDataBundle extends Bundle {
  val data = UInt(64.W)
  val resp = UInt(2.W)
}

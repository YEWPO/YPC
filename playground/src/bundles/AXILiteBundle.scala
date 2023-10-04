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

class AXILiteWriteAddrBundle extends Bundle {
  val addr = UInt(64.W)
  val prot = UInt(3.W)
}

class AXILiteWriteDataBundle extends Bundle {
  val data = UInt(64.W)
  val strb = UInt(8.W)
}

class AXILiteWriteRespBundle extends Bundle {
  val resp = Bool()
}

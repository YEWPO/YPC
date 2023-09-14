package bundles

import chisel3._

class PreIFDataBundle extends Bundle {
  val pc = UInt(64.W)
}

class PreIFControlBundle extends Bundle {}

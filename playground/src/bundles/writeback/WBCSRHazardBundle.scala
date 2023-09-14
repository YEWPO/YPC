package bundles.writeback

import chisel3._

class WBCSRHazardDataBundle extends Bundle {
  val csr_w_addr     = UInt(12.W)
  val csr_w_addr_tag = Bool()
}

class WBCSRHazardControlBundle extends Bundle {}

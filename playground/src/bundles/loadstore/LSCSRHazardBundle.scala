package bundles.loadstore

import chisel3._

class LSCSRHazardDataBundle extends Bundle {
  val csr_w_addr     = UInt(12.W)
  val csr_w_addr_tag = Bool()
}

class LSCSRHazardControlBundle extends Bundle {}

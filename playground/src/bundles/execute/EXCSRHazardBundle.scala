package bundles.execute

import chisel3._

class EXCSRHazardDataBundle extends Bundle {
  val csr_w_addr     = UInt(12.W)
  val csr_w_addr_tag = Bool()
}

class EXCSRHazardControlBundle extends Bundle {}

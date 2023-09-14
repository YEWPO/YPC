package bundles.instdecode

import chisel3._

class IDCSRHazardDataBundle extends Bundle {
  val csr_r_addr = UInt(12.W)
  val csr_w_addr = UInt(12.W)

  val epc  = UInt(64.W)
  val tvec = UInt(64.W)

  val csr_r_addr_tag = Bool()
  val csr_w_addr_tag = Bool()

  val ecall_op = Bool()
  val mret_op  = Bool()
}

class IDCSRHazardControlBundle extends Bundle {
  val csr_forward_ctl = UInt(2.W)
}

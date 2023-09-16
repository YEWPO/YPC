package bundles.instdecode

import chisel3._

class IDForwardBundle extends Bundle {
  val exe_E   = UInt(64.W)
  val snpc_E  = UInt(64.W)
  val exe_M   = UInt(64.W)
  val mem_M   = UInt(64.W)
  val snpc_M  = UInt(64.W)
  val wb_data = UInt(64.W)
}

class IDCSRForwardBundle extends Bundle {
  val csr_data_E = UInt(64.W)
  val csr_data_M = UInt(64.W)
  val csr_data_W = UInt(64.W)
}

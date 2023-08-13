package entity

import chisel3._

class LoadStoreForward extends Bundle {
  val exe_out = Output(UInt(64.W))
  val mem_out = Output(UInt(64.W))
  val snpc    = Output(UInt(64.W))
}

class LoadStoreCSRForward extends Bundle {
  val csr_exe_out = Output(UInt(64.W))
}

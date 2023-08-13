package entity

import chisel3._

class ExecuteForward extends Bundle {
  val exe_out = Output(UInt(64.W))
  val snpc    = Output(UInt(64.W))
}

class ExecuteCSRForward extends Bundle {
  val csr_exe_out = Output(UInt(64.W))
}

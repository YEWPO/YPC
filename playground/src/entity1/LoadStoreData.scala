package entity

import chisel3._

class LoadStoreData extends Bundle {
  val exe_out = Output(UInt(64.W))
  val mem_out = Output(UInt(64.W))
  val rd      = Output(UInt(5.W))
  val inst    = Output(UInt(32.W))
  val pc      = Output(UInt(64.W))
  val snpc    = Output(UInt(64.W))
  val dnpc    = Output(UInt(64.W))
}

class LoadStoreCSRData extends Bundle {
  val csr_w_data = Output(UInt(64.W))
  val csr_w_addr = Output(UInt(12.W))
}

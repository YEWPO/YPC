package entity

import chisel3._

class WriteBackHazard extends Bundle {
  val rd     = Output(UInt(5.W))
  val rd_tag = Output(Bool())
}

class WriteBackCSRHazard extends Bundle {
  val csr_w_addr     = Output(UInt(12.W))
  val csr_w_addr_tag = Output(Bool())
}

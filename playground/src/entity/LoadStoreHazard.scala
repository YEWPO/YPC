package entity

import chisel3._

class LoadStoreHazard extends Bundle {
  val wb_ctl = Output(UInt(2.W))
  val rd     = Output(UInt(5.W))
  val rd_tag = Output(Bool())
}

class LoadStoreCSRHazard extends Bundle {
  val csr_w_addr     = Output(UInt(12.W))
  val csr_w_addr_tag = Output(Bool())
}

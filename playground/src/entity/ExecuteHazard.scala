package entity

import chisel3._

class ExecuteHazard extends Bundle {
  val rd       = Output(UInt(5.W))
  val jump_sig = Output(Bool())
  val wb_ctl   = Output(UInt(2.W))
  val rd_tag   = Output(Bool())

  val reset = Input(Bool())
}

class ExecuteCSRHazard extends Bundle {
  val csr_w_addr     = Output(UInt(12.W))
  val csr_w_addr_tag = Output(Bool())
}

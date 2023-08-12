package entity

import chisel3._

class InstDecodeHazard extends Bundle {
  val rs1 = Output(UInt(5.W))
  val rs2 = Output(UInt(5.W))

  val rs1_tag = Output(Bool())
  val rs2_tag = Output(Bool())

  val reset  = Input(Bool())
  val enable = Input(Bool())

  val fa_ctl = Input(UInt(3.W))
  val fb_ctl = Input(UInt(3.W))
}

class InstDecodeCSRHazard extends Bundle {
  val csr_r_addr     = Output(UInt(12.W))
  val csr_r_addr_tag = Output(Bool())

  val csr_forward_ctl = Input(UInt(2.W))
}

package entity

import chisel3._

class InstDecodeHazard extends Bundle {
  val rs1 = Output(UInt(5.W))
  val rs2 = Output(UInt(5.W))

  val reset  = Input(Bool())
  val enable = Input(Bool())

  val fa_ctl = Input(UInt(3.W))
  val fb_ctl = Input(UInt(3.W))
}

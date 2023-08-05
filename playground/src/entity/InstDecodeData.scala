package entity

import chisel3._

class InstDecodeData extends Bundle {
  val imm  = Output(UInt(64.W))
  val src1 = Output(UInt(64.W))
  val src2 = Output(UInt(64.W))
  val rd   = Output(UInt(5.W))
  val inst = Output(UInt(32.W))
  val pc   = Output(UInt(64.W))
  val snpc = Output(UInt(64.W))
}

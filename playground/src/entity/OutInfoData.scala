package entity

import chisel3._

class OutInfoData extends Bundle {
  val ebreak_op  = Output(Bool())
  val invalid_op = Output(Bool())
  val pc         = Output(UInt(64.W))
  val inst       = Output(UInt(32.W))
}

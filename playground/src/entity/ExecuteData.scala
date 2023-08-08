package entity

import chisel3._

class ExecuteData extends Bundle {
  val exe_out = Output(UInt(64.W))
  val src2    = Output(UInt(64.W))
  val rd      = Output(UInt(5.W))
  val inst    = Output(UInt(32.W))
  val pc      = Output(UInt(64.W))
  val snpc    = Output(UInt(64.W))
  val dnpc    = Output(UInt(64.W))
}

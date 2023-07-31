package entity

import chisel3._

class ExecuteData extends Bundle {
  val alu_out = Output(UInt(64.W))
  val r_data2 = Output(UInt(64.W))
  val rd      = Output(UInt(5.W))
  val inst    = Output(UInt(32.W))
  val pc      = Output(UInt(64.W))
  val snpc    = Output(UInt(64.W))
}

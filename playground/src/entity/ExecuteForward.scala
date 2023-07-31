package entity

import chisel3._

class ExecuteForward extends Bundle {
  val alu_out = Output(UInt(64.W))
  val snpc    = Output(UInt(64.W))
}

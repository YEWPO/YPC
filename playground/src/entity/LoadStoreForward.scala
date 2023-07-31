package entity

import chisel3._

class LoadStoreForward extends Bundle {
  val alu_out = Output(UInt(64.W))
  val mem_out = Output(UInt(64.W))
  val snpc    = Output(UInt(64.W))
}

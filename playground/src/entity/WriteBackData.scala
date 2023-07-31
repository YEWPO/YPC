package entity

import chisel3._

class WriteBackData extends Bundle {
  val wb_data = Output(UInt(64.W))
  val rd      = Output(UInt(5.W))
}

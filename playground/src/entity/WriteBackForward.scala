package entity

import chisel3._

class WriteBackForward extends Bundle {
  val wb_data = Output(UInt(64.W))
}

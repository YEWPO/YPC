package entity

import chisel3._

class WriteBackHazard extends Bundle {
  val rd = Output(UInt(5.W))
}

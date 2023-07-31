package entity

import chisel3._

class InstFetchData extends Bundle {
  val inst = Output(UInt(32.W))
  val pc   = Output(UInt(64.W))
  val snpc = Output(UInt(64.W))
}

package utils

import chisel3._
import chisel3.util._

class DataMemIO extends Bundle {
  val addr   = Input(UInt(64.W))
  val w_data = Input(UInt(64.W))
  val mask = Input(UInt(64.W))

  val r_data = Output(UInt(64.W))
}

class DataMem extends BlackBox with HasBlackBoxPath {
  val io = IO(new DataMemIO)

  addPath("playground/src/utils/DataMem.v")
}

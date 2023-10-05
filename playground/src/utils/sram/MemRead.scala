package utils.sram

import chisel3._
import chisel3.util._

class MemRead extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val addr   = Input(UInt(64.W))
    val r_data = Output(UInt(64.W))
  })

  addPath("playground/src/utils/sram/MemRead.v")
}

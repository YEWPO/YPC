package utils.loadstore

import chisel3._
import chisel3.util._

class MemWrite extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val w_en   = Input(Bool())
    val addr   = Input(UInt(64.W))
    val w_data = Input(UInt(64.W))
    val mask   = Input(UInt(8.W))
  })

  addPath("playground/src/utils/loadstore/MemWrite.v")
}

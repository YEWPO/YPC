package utils

import chisel3._
import chisel3.util._

class MemWrite extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val w_en   = Input(Bool())
    val addr   = Input(UInt(64.W))
    val w_data = Input(UInt(64.W))
    val mask   = Input(UInt(64.W))
  })

  addPath("playground/src/utils/MemWrite.v")
}
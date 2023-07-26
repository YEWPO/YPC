package utils

import chisel3._
import chisel3.util._

class InstMem extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val addr = Input(UInt(64.W))
    val inst = Output(UInt(32.W))
  })

  addPath("playground/src/utils/InstMem.v")
}

package utils

import chisel3._
import chisel3.util._

class Ebreak extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val clock = Input(Clock())
    val in = Input(Bool())
  })

  addPath("playground/src/utils/Ebreak.v")
}

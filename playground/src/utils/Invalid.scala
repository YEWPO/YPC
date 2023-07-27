package utils

import chisel3._
import chisel3.util._

class Invalid extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val in = Input(Bool())
  })

  addPath("playground/src/utils/Invalid.v")
}

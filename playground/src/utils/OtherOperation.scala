package utils

import chisel3._
import chisel3.util._

class OtherOperation extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val ebreak_op  = Input(Bool())
    val invalid_op = Input(Bool())
    val pc         = Input(UInt(64.W))
  })

  addPath("playground/src/utils/OtherOperation.v")
}

package utils

import chisel3._
import chisel3.util._

class DebugPartIO extends Bundle {
  val pc   = Input(UInt(64.W))
  val inst = Input(UInt(32.W))
  val dnpc = Input(UInt(64.W))
}

class DebugPart extends BlackBox with HasBlackBoxPath {
  val io = IO(new DebugPartIO)

  addPath("playground/src/utils/DebugPart.v")
}

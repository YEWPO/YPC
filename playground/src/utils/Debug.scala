package utils

import chisel3._
import chisel3.util._

class DebugIO extends Bundle {
  val pc   = Input(UInt(64.W))
  val inst = Input(UInt(32.W))
}

class Debug extends BlackBox with HasBlackBoxPath {
  val io = IO(new DebugIO)

  addPath("playground/src/utils/Debug.v")
}

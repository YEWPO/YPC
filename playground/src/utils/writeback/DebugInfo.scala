package utils.writeback

import chisel3._
import chisel3.util._

class DebugInfoIO extends Bundle {
  val pc   = Input(UInt(64.W))
  val inst = Input(UInt(32.W))
  val dnpc = Input(UInt(64.W))
}

class DebugInfo extends BlackBox with HasBlackBoxPath {
  val io = IO(new DebugInfoIO)

  addPath("playground/src/utils/writeback/DebugPart.v")
}

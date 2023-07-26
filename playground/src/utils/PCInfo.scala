package utils

import chisel3._
import chisel3.util._

class PCInfo extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val pc_val = Input(UInt(64.W))
  })

  addPath("playground/src/utils/PCInfo.v")
}

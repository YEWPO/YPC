package utils

import chisel3._
import chisel3.util._

/**
  * register DPI-C
  */
class RegsInfo extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val inbits = Input(UInt((64 * 32).W))
  })

  addPath("playground/src/utils/RegsInfo.v")
}

package utils.instdecode

import chisel3._
import chisel3.util._

/**
  * register DPI-C
  */
class GPRInfo extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val inbits = Input(UInt((64 * 32).W))
  })

  addPath("playground/src/utils/instdecode/GPRInfo.v")
}

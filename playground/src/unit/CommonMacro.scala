package unit

import chisel3._
import chisel3.util._

object CommonMacro {
  val PC_RESET_VAL   = "h8000_0000".U(64.W)
  val INST_RESET_VAL = "h13".U(32.W) // NOP instruction

  def signExtend(src: UInt): UInt = {
    val src_len    = src.getWidth
    val extend_len = 64 - src_len
    Cat(Fill(extend_len, src(src_len - 1)), src)
  }
}

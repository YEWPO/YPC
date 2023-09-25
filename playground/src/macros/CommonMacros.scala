package macros

import chisel3._
import chisel3.util._

object CommonMacros {
  val NORMAL_RESET_VAL = 0.U
  val PC_RESET_VAL     = "h8000_0000".U(64.W)
  val INST_RESET_VAL   = "h13".U(32.W) // NOP instruction
  val CAUSE_RESET_VAL  = "hffff_ffff_ffff_ffff".U(64.W)

  val MSTATUS_RESET_VAL = "ha00001800".U(64.W)

  def signExtend(src: UInt): UInt = {
    val src_len    = src.getWidth
    val extend_len = 64 - src_len
    Cat(Fill(extend_len, src(src_len - 1)), src)
  }
  def zeroExtend(src: UInt): UInt = {
    val src_len    = src.getWidth
    val extend_len = 64 - src_len
    Cat(Fill(extend_len, 0.U(1.W)), src)
  }

  def getWord(src:     UInt, pos: Int): UInt = src(32 * pos + 31, 32 * pos)
  def getHalfWord(src: UInt, pos: Int): UInt = src(16 * pos + 15, 16 * pos)
  def getByte(src:     UInt, pos: Int): UInt = src(8 * pos + 7, 8 * pos)
}

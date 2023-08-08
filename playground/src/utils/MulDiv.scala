package utils

import chisel3._
import unit._
import control._
import chisel3.util._

class MulDivIO extends Bundle {
  val src1    = Input(UInt(64.W))
  val src2    = Input(UInt(64.W))
  val mul_ctl = Input(UInt(4.W))
  val mul_out = Output(UInt(64.W))
}

class MulDiv extends Module {
  val io = IO(new MulDivIO)

  def getDoubleWord(src: UInt, n: Int): UInt = src(64 * n + 63, 64 * n)

  val mul   = (io.src1.asSInt * io.src2.asSInt).asUInt
  val mulsu = (io.src1.asSInt * io.src2).asUInt
  val mulu  = io.src1 * io.src2
  val mulw  = (CommonMacro.getWord(io.src1, 0).asSInt * CommonMacro.getWord(io.src2, 0).asSInt).asUInt

  val div   = (io.src1.asSInt / io.src2.asSInt).asUInt
  val divu  = io.src1 / io.src2
  val rem   = (io.src1.asSInt % io.src2.asSInt).asUInt
  val remu  = io.src1 % io.src2
  val divw  = (CommonMacro.getWord(io.src1, 0).asSInt / CommonMacro.getWord(io.src2, 0).asSInt).asUInt
  val divuw = CommonMacro.getWord(io.src1, 0) / CommonMacro.getWord(io.src2, 0)
  val remw  = (CommonMacro.getWord(io.src1, 0).asSInt % CommonMacro.getWord(io.src2, 0).asSInt).asUInt
  val remuw = CommonMacro.getWord(io.src1, 0) % CommonMacro.getWord(io.src2, 0)

  val mul_map = Seq(
    ControlMacro.MUL_CTL_MUL    -> getDoubleWord(mul, 0),
    ControlMacro.MUL_CTL_MULH   -> getDoubleWord(mul, 1),
    ControlMacro.MUL_CTL_MULHSU -> getDoubleWord(mulsu, 1),
    ControlMacro.MUL_CTL_MULHU  -> getDoubleWord(mulu, 1),
    ControlMacro.MUL_CTL_MULW   -> CommonMacro.signExtend(CommonMacro.getWord(mulw, 0)),
    ControlMacro.MUL_CTL_DIV    -> div,
    ControlMacro.MUL_CTL_DIVU   -> divu,
    ControlMacro.MUL_CTL_REM    -> rem,
    ControlMacro.MUL_CTL_REMU   -> remu,
    ControlMacro.MUL_CTL_DIVW   -> CommonMacro.signExtend(divw),
    ControlMacro.MUL_CTL_DIVUW  -> CommonMacro.signExtend(divuw),
    ControlMacro.MUL_CTL_REMW   -> CommonMacro.signExtend(remw),
    ControlMacro.MUL_CTL_REMUW  -> CommonMacro.signExtend(remuw)
  )

  io.mul_out := MuxLookup(io.mul_ctl, 0.U(64.W))(mul_map)
}

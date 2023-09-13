package utils.execute

import chisel3._
import chisel3.util._
import macros._

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
  val mulw  = (CommonMacros.getWord(io.src1, 0).asSInt * CommonMacros.getWord(io.src2, 0).asSInt).asUInt

  val div   = (io.src1.asSInt / io.src2.asSInt).asUInt
  val divu  = io.src1 / io.src2
  val rem   = (io.src1.asSInt % io.src2.asSInt).asUInt
  val remu  = io.src1 % io.src2
  val divw  = (CommonMacros.getWord(io.src1, 0).asSInt / CommonMacros.getWord(io.src2, 0).asSInt).asUInt
  val divuw = CommonMacros.getWord(io.src1, 0) / CommonMacros.getWord(io.src2, 0)
  val remw  = (CommonMacros.getWord(io.src1, 0).asSInt % CommonMacros.getWord(io.src2, 0).asSInt).asUInt
  val remuw = CommonMacros.getWord(io.src1, 0) % CommonMacros.getWord(io.src2, 0)

  val mul_map = Seq(
    ControlMacros.MUL_CTL_MUL    -> getDoubleWord(mul, 0),
    ControlMacros.MUL_CTL_MULH   -> getDoubleWord(mul, 1),
    ControlMacros.MUL_CTL_MULHSU -> getDoubleWord(mulsu, 1),
    ControlMacros.MUL_CTL_MULHU  -> getDoubleWord(mulu, 1),
    ControlMacros.MUL_CTL_MULW   -> CommonMacros.signExtend(CommonMacros.getWord(mulw, 0)),
    ControlMacros.MUL_CTL_DIV    -> div,
    ControlMacros.MUL_CTL_DIVU   -> divu,
    ControlMacros.MUL_CTL_REM    -> rem,
    ControlMacros.MUL_CTL_REMU   -> remu,
    ControlMacros.MUL_CTL_DIVW   -> CommonMacros.signExtend(divw),
    ControlMacros.MUL_CTL_DIVUW  -> CommonMacros.signExtend(divuw),
    ControlMacros.MUL_CTL_REMW   -> CommonMacros.signExtend(remw),
    ControlMacros.MUL_CTL_REMUW  -> CommonMacros.signExtend(remuw)
  )

  val mul_out = MuxLookup(io.mul_ctl, 0.U(64.W))(mul_map)

  val spec_map = Array(
    // div, divu, divw, divuw
    (io.mul_ctl(2, 1) === "b10".U && io.src2 === 0.U) -> "hffff_ffff_ffff_ffff".U,
    // rem, remu
    (io.mul_ctl(3, 1) === "b011".U && io.src2 === 0.U) -> io.src1,
    // rem, remu
    (io.mul_ctl(3, 1) === "b111".U && io.src2 === 0.U) -> CommonMacros.signExtend(CommonMacros.getWord(io.src1, 0)),
    // div
    (io.mul_ctl === ControlMacros.MUL_CTL_DIV
      && io.src1 === "h8000_0000_0000_0000".U
      && io.src2 === "hffff_ffff_ffff_ffff".U) -> io.src1,
    // divw
    (io.mul_ctl === ControlMacros.MUL_CTL_DIVW
      && CommonMacros.getWord(io.src1, 0) === "h8000_0000".U
      && CommonMacros.getWord(io.src2, 0) === "hffff_ffff".U) -> "hffff_ffff_8000_0000".U,
    // rem
    (io.mul_ctl === ControlMacros.MUL_CTL_REM
      && io.src1 === "h8000_0000_0000_0000".U
      && io.src2 === "hffff_ffff_ffff_ffff".U) -> 0.U,
    // remw
    (io.mul_ctl === ControlMacros.MUL_CTL_REMW
      && CommonMacros.getWord(io.src1, 0) === "h8000_0000".U
      && CommonMacros.getWord(io.src2, 0) === "hffff_ffff".U) -> 0.U
  )

  io.mul_out := MuxCase(mul_out, spec_map)
}

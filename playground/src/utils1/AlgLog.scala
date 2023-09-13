package utils

import chisel3._
import chisel3.util._
import unit._
import control._

object AlgLogMacro {
  val WORD_TAG   = 4
  val SUB_OP_TAG = 0
}

class AlgLogIO extends Bundle {
  val src1    = Input(UInt(64.W))
  val src2    = Input(UInt(64.W))
  val alu_ctl = Input(UInt(5.W))

  val alu_out = Output(UInt(64.W))
}

/**
  * alu_ctl bit map
  *
  *  4           4 3           1 0          0
  * +-------------+-------------+------------+
  * | word op tag | alu op code | sub op tag |
  * +-------------+-------------+------------+
  */
class AlgLog extends Module {
  val io = IO(new AlgLogIO)

  // check if it is a word operation
  val src1  = Mux(io.alu_ctl(AlgLogMacro.WORD_TAG).orR, CommonMacro.signExtend(CommonMacro.getWord(io.src1, 0)), io.src1)
  val src2  = Mux(io.alu_ctl(AlgLogMacro.WORD_TAG).orR, CommonMacro.signExtend(CommonMacro.getWord(io.src2, 0)), io.src2)
  val shamt = Mux(io.alu_ctl(AlgLogMacro.WORD_TAG).orR, Cat(0.U(1.W), src2(4, 0)), src2(5, 0))

  // calcuate
  val add  = src1 + src2
  val sub  = src1 - src2
  val xor  = src1 ^ src2
  val or   = src1 | src2
  val and  = src1 & src2
  val slt  = src1.asSInt < src2.asSInt
  val sge  = !slt
  val sltu = src1 < src2
  val sgeu = !sltu
  val equ  = xor.orR === 0.U
  val neq  = !equ
  val sll  = src1 << shamt
  val srl  = src1 >> shamt
  val sra  = (src1.asSInt >> shamt).asUInt
  val mova = src1
  val movb = src2

  val srlw_mask = "hffff_ffff".U >> shamt

  val alu_map = Seq(
    ControlMacro.ALU_CTL_DEFAULT -> add,
    ControlMacro.ALU_CTL_ADD     -> add,
    ControlMacro.ALU_CTL_ADDW    -> CommonMacro.signExtend(CommonMacro.getWord(add, 0)),
    ControlMacro.ALU_CTL_SUB     -> sub,
    ControlMacro.ALU_CTL_SUBW    -> CommonMacro.signExtend(CommonMacro.getWord(sub, 0)),
    ControlMacro.ALU_CTL_XOR     -> xor,
    ControlMacro.ALU_CTL_OR      -> or,
    ControlMacro.ALU_CTL_AND     -> and,
    ControlMacro.ALU_CTL_SLT     -> slt,
    ControlMacro.ALU_CTL_SGE     -> sge,
    ControlMacro.ALU_CTL_SLTU    -> sltu,
    ControlMacro.ALU_CTL_SGEU    -> sgeu,
    ControlMacro.ALU_CTL_EQU     -> equ,
    ControlMacro.ALU_CTL_NEQ     -> neq,
    ControlMacro.ALU_CTL_SLL     -> sll,
    ControlMacro.ALU_CTL_SLLW    -> CommonMacro.signExtend(CommonMacro.getWord(sll, 0)),
    ControlMacro.ALU_CTL_SRL     -> srl,
    ControlMacro.ALU_CTL_SRLW    -> CommonMacro.signExtend(CommonMacro.getWord(srl, 0) & srlw_mask),
    ControlMacro.ALU_CTL_SRA     -> sra,
    ControlMacro.ALU_CTL_SRAW    -> CommonMacro.signExtend(CommonMacro.getWord(sra, 0)),
    ControlMacro.ALU_CTL_MOVA    -> mova,
    ControlMacro.ALU_CTL_MOVB    -> movb
  )

  io.alu_out := MuxLookup(io.alu_ctl, 0.U(64.W))(alu_map)
}

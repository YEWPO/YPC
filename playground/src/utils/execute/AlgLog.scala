package utils.execute

import chisel3._
import chisel3.util._
import macros._

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
  val src1 =
    Mux(io.alu_ctl(AlgLogMacro.WORD_TAG).orR, CommonMacros.signExtend(CommonMacros.getWord(io.src1, 0)), io.src1)
  val src2 =
    Mux(io.alu_ctl(AlgLogMacro.WORD_TAG).orR, CommonMacros.signExtend(CommonMacros.getWord(io.src2, 0)), io.src2)
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
    ControlMacros.ALU_CTL_DEFAULT -> add,
    ControlMacros.ALU_CTL_ADD     -> add,
    ControlMacros.ALU_CTL_ADDW    -> CommonMacros.signExtend(CommonMacros.getWord(add, 0)),
    ControlMacros.ALU_CTL_SUB     -> sub,
    ControlMacros.ALU_CTL_SUBW    -> CommonMacros.signExtend(CommonMacros.getWord(sub, 0)),
    ControlMacros.ALU_CTL_XOR     -> xor,
    ControlMacros.ALU_CTL_OR      -> or,
    ControlMacros.ALU_CTL_AND     -> and,
    ControlMacros.ALU_CTL_SLT     -> slt,
    ControlMacros.ALU_CTL_SGE     -> sge,
    ControlMacros.ALU_CTL_SLTU    -> sltu,
    ControlMacros.ALU_CTL_SGEU    -> sgeu,
    ControlMacros.ALU_CTL_EQU     -> equ,
    ControlMacros.ALU_CTL_NEQ     -> neq,
    ControlMacros.ALU_CTL_SLL     -> sll,
    ControlMacros.ALU_CTL_SLLW    -> CommonMacros.signExtend(CommonMacros.getWord(sll, 0)),
    ControlMacros.ALU_CTL_SRL     -> srl,
    ControlMacros.ALU_CTL_SRLW    -> CommonMacros.signExtend(CommonMacros.getWord(srl, 0) & srlw_mask),
    ControlMacros.ALU_CTL_SRA     -> sra,
    ControlMacros.ALU_CTL_SRAW    -> CommonMacros.signExtend(CommonMacros.getWord(sra, 0)),
    ControlMacros.ALU_CTL_MOVA    -> mova,
    ControlMacros.ALU_CTL_MOVB    -> movb
  )

  io.alu_out := MuxLookup(io.alu_ctl, 0.U(64.W))(alu_map)
}

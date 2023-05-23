package decode

import execute.Ebreak

import chisel3._

class Decoder extends Module {
  val io = IO(new Bundle {
    val inst = Input(UInt(32.W))
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))
    val pc = Input(UInt(64.W))

    val out_src1 = Output(UInt(64.W))
    val out_src2 = Output(UInt(64.W))

    val word_op = Output(Bool())
    val sub_op = Output(Bool())

    val R_type = Output(Bool())
    val I_type = Output(Bool())
    val S_type = Output(Bool())
    val B_type = Output(Bool())
    val U_type = Output(Bool())
    val J_type = Output(Bool())
  })

  val op_type_decoder = Module(new OperationType)
  val imm_part = Module(new ImmPart)
  val src_part = Module(new SrcPart)
  val sub_op_decoder = Module(new SubOp)
  val type_oh = Module(new TypeOH)

  val ebreak = Module(new Ebreak)

  op_type_decoder.io.operation := io.inst(6, 0)

  imm_part.io.inst := io.inst
  imm_part.io.op_type := op_type_decoder.io.operation_type

  sub_op_decoder.io.inst := io.inst

  type_oh.io.op_type := op_type_decoder.io.operation_type

  ebreak.io.op_type := op_type_decoder.io.operation_type

  src_part.io.op_type := op_type_decoder.io.operation_type
  src_part.io.src1 := io.src1
  src_part.io.src2 := io.src2
  src_part.io.pc := Mux(io.inst(6, 0) === "b0110111".U, 0.U(64.W), io.pc)
  src_part.io.imm := imm_part.io.imm_out
  src_part.io.word_op := op_type_decoder.io.word_op

  io.out_src1 := src_part.io.out_src1
  io.out_src2 := src_part.io.out_src2

  io.word_op := op_type_decoder.io.word_op
  io.sub_op := sub_op_decoder.io.sub_op

  io.R_type := type_oh.io.R_type
  io.I_type := type_oh.io.I_type
  io.S_type := type_oh.io.S_type
  io.B_type := type_oh.io.B_type
  io.U_type := type_oh.io.U_type
  io.J_type := type_oh.io.J_type
}

package decode

import chisel3._
import chisel3.util._

class SrcPart extends Module {
  val io = IO(new Bundle {
    val op_type = Input(UInt(64.W))
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))
    val pc = Input(UInt(64.W))
    val imm = Input(UInt(64.W))
    val word_op = Input(Bool())

    val out_src1 = Output(UInt(64.W))
    val out_src2 = Output(UInt(64.W))
  })

  val a_selector = (
    (io.op_type === "b011".U)
    || (io.op_type === "b100".U)
    || (io.op_type === "b101".U)
  )
  val b_selector = (io.op_type =/= "b000".U)

  val a_out = Mux(a_selector, io.pc, io.src1)
  val b_out = Mux(b_selector, io.imm, io.src2)

  io.out_src1 := Mux(io.word_op, Cat(Fill(32, a_out(31)), a_out(31, 0)), a_out)
  io.out_src2 := Mux(io.word_op, Cat(Fill(32, b_out(31)), b_out(31, 0)), b_out)
}

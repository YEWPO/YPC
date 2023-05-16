package execute

import chisel3._
import chisel3.util._

class Calculator extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))

    val funct = Input(UInt(3.W))

    val word_op = Input(Bool())

    val subop_type = Input(Bool())

    val res = Output(UInt(64.W))
  })

  val add = Module(new Add)
  val leftshift = Module(new Leftshift)
  val slt = Module(new Slt)
  val sltu = Module(new Sltu)
  val xor = Module(new Xor)
  val rightshift = Module(new Rightshift)
  val or = Module(new Or)
  val and = Module(new And)

  add.io.src1 := io.src1
  add.io.src2 := io.src2
  add.io.add_op := io.subop_type

  leftshift.io.src1 := io.src1
  leftshift.io.src2 := io.src2
  leftshift.io.word_op := io.word_op

  slt.io.src1 := io.src1.asSInt
  slt.io.src2 := io.src2.asSInt

  sltu.io.src1 := io.src1
  sltu.io.src2 := io.src2

  xor.io.src1 := io.src1
  xor.io.src2 := io.src2

  rightshift.io.src1 := io.src1
  rightshift.io.src2 := io.src2
  rightshift.io.word_op := io.word_op
  rightshift.io.sr_op := io.subop_type

  or.io.src1 := io.src1
  or.io.src2 := io.src2

  and.io.src1 := io.src1
  and.io.src2 := io.src2

  val res = MuxCase(0.U(64.W), Array (
    (io.funct === "b000".U) -> add.io.res,
    (io.funct === "b001".U) -> leftshift.io.res,
    (io.funct === "b010".U) -> slt.io.res,
    (io.funct === "b011".U) -> sltu.io.res,
    (io.funct === "b100".U) -> xor.io.res,
    (io.funct === "b101".U) -> rightshift.io.res,
    (io.funct === "b110".U) -> or.io.res,
    (io.funct === "b111".U) -> and.io.res
  ))

  val w_res = Cat(Fill(32, res(31)), res(31, 0))

  io.res := Mux(io.word_op, w_res, res)
}

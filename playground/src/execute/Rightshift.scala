package execute

import chisel3._
import chisel3.util._

class Rightshift extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))
    val sr_op = Input(Bool())
    val word_op = Input(Bool())
    val res = Output(UInt(64.W))
  })

  val rightshift32 = Module(new Rightshift32)
  val rightshift64 = Module(new Rightshift64)

  rightshift32.io.src1 := io.src1(31, 0)
  rightshift32.io.src2 := io.src2(4, 0)
  rightshift32.io.sr_op := io.sr_op

  rightshift64.io.src1 := io.src1
  rightshift64.io.src2 := io.src2(5, 0)
  rightshift64.io.sr_op := io.sr_op

  io.res := Mux(io.word_op, rightshift32.io.res, rightshift64.io.res)
}

class Rightshift32 extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(32.W))
    val src2 = Input(UInt(5.W))
    val sr_op = Input(Bool())
    val res = Output(UInt(64.W))
  })

  val l_res = io.src1 >> io.src2
  val a_res = (io.src1.asSInt >> io.src2).asUInt

  val res = Mux(io.sr_op, a_res, l_res)

  io.res := Cat(Fill(32, res(31)), res(31, 0))
}

class Rightshift64 extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(6.W))
    val sr_op = Input(Bool())
    val res = Output(UInt(64.W))
  })

  val l_res = io.src1 >> io.src2
  val a_res = (io.src1.asSInt >> io.src2).asUInt

  io.res := Mux(io.sr_op, a_res.asUInt, l_res)
}

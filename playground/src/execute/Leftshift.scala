package execute

import chisel3._
import chisel3.util._

class Leftshift extends Module {
  val io = IO(new Bundle{
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))
    val word_op = Input(Bool())
    val res = Output(UInt(64.W))
  })

  val leftshift32 = Module(new Leftshift32)
  val leftshift64 = Module(new Leftshift64)

  leftshift32.io.src1 := io.src1(31, 0)
  leftshift32.io.src2 := io.src2(4, 0)

  leftshift64.io.src1 := io.src1
  leftshift64.io.src2 := io.src2(5, 0)

  io.res := Mux(io.word_op, leftshift32.io.res, leftshift64.io.res)
}

class Leftshift32 extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(32.W))
    val src2 = Input(UInt(5.W))
    val res = Output(UInt(64.W))
  })

  val res = io.src1 << io.src2
  
  io.res := Cat(Fill(32, res(31)), res(31, 0))
}

class Leftshift64 extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(6.W))
    val res = Output(UInt(64.W))
  })

  io.res := io.src1 << io.src2
}

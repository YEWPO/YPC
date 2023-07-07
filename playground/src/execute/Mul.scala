package execute

import chisel3._
import chisel3.util._

class Mul extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))

    val funct = Input(UInt(3.W))

    val word_op = Input(Bool())

    val res = Output(UInt(64.W))
  })

  val mulop = Module(new Mulop)
  val divop = Module(new Divop)
  val divuop = Module(new DivUop)
  val remop = Module(new Remop)
  val remuop = Module(new RemUop)

  mulop.io.src1 := io.src1
  mulop.io.src2 := io.src2
  mulop.io.funct := io.funct
  mulop.io.word_op := io.word_op

  divop.io.src1 := io.src1
  divop.io.src2 := io.src2
  divop.io.word_op := io.word_op

  divuop.io.src1 := io.src1
  divuop.io.src2 := io.src2
  divuop.io.word_op := io.word_op

  remop.io.src1 := io.src1
  remop.io.src2 := io.src2
  remop.io.word_op := io.word_op

  remuop.io.src1 := io.src1
  remuop.io.src2 := io.src2
  remuop.io.word_op := io.word_op

  io.res := MuxCase(mulop.io.res, Array(
    (io.funct === "b100".U) -> divop.io.res,
    (io.funct === "b101".U) -> divuop.io.res,
    (io.funct === "b110".U) -> remop.io.res,
    (io.funct === "b111".U) -> remuop.io.res,
    ))
}

class Mulop extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))

    val funct = Input(UInt(3.W))

    val word_op = Input(Bool())

    val res = Output(UInt(64.W))
  })

  val src1_1 = Mux(io.word_op, Cat(Fill(32, io.src1(31)), io.src1(31, 0)), io.src1)
  val src2_1 = Mux(io.word_op, Cat(Fill(32, io.src2(31)), io.src2(31, 0)), io.src2)

  val src1_2 = Mux(io.funct === "b010".U, Cat(Fill(64, 0.U(1.W)), src1_1(63, 0)), Cat(Fill(64, src1_1(63)), src1_1(63, 0)))
  val src2_2 = Mux(io.funct === "b010".U || io.funct === "b011".U, Cat(Fill(64, 0.U(1.W)), src2_1(63, 0)), Cat(Fill(64, src2_1(63)), src2_1(63, 0)))

  val result = src1_2 * src2_2

  val res = Mux(io.funct === "b000".U, result(63, 0), result(127, 64))

  io.res := Mux(io.word_op, Cat(Fill(32, res(31)), res(31, 0)), res(63, 0))
}

class Divop extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))

    val word_op = Input(Bool())

    val res = Output(UInt(64.W))
  })

  val res_32 = MuxCase((io.src1(31, 0).asSInt / io.src2(31, 0).asSInt).asUInt, Array(
    (io.src2(31, 0) === 0.U) -> "hffff_ffff".U(32.W),
    ((io.src1(31, 0) === "h8000_0000".U) && (io.src2(31, 0) === "hffff_ffff".U)) -> "h8000_0000".U(32.W)
    ))
  val res_64 = MuxCase((io.src1.asSInt / io.src2.asSInt).asUInt, Array(
    (io.src2 === 0.U) -> "hffff_ffff_ffff_ffff".U(64.W),
    ((io.src1 === "h8000_0000_0000_0000".U) && (io.src2 === "hffff_ffff_ffff_ffff".U)) -> "h8000_0000_0000_0000".U(64.W)
    ))

  io.res := Mux(io.word_op, Cat(Fill(32, res_32(31)), res_32(31, 0)), res_64(63, 0))
}

class DivUop extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))

    val word_op = Input(Bool())

    val res = Output(UInt(64.W))
  })

  val res_32 = MuxCase(io.src1(31, 0) / io.src2(31, 0), Array(
    (io.src2(31, 0) === 0.U) -> "hffff_ffff".U(32.W)
    ))
  val res_64 = MuxCase(io.src1 / io.src2, Array(
    (io.src2 === 0.U) -> "hffff_ffff_ffff_ffff".U(64.W)
    ))

  io.res := Mux(io.word_op, Cat(Fill(32, res_32(31)), res_32(31, 0)), res_64(63, 0))
}

class Remop extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))

    val word_op = Input(Bool())

    val res = Output(UInt(64.W))
  })

  val res_32 = MuxCase((io.src1(31, 0).asSInt % io.src2(31, 0).asSInt).asUInt, Array(
    (io.src2(31, 0) === 0.U) -> io.src1(31, 0),
    ((io.src1(31, 0) === "h8000_0000".U) && (io.src2(31, 0) === "hffff_ffff".U)) -> 0.U(32.W)
    ))
  val res_64 = MuxCase((io.src1.asSInt % io.src2.asSInt).asUInt, Array(
    (io.src2 === 0.U) -> io.src1,
    ((io.src1 === "h8000_0000_0000_0000".U) && (io.src2 === "hffff_ffff_ffff_ffff".U)) -> 0.U(64.W)
    ))

  io.res := Mux(io.word_op, Cat(Fill(32, res_32(31)), res_32(31, 0)), res_64(63, 0))
}

class RemUop extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))

    val word_op = Input(Bool())

    val res = Output(UInt(64.W))
  })

  val res_32 = MuxCase(io.src1(31, 0) % io.src2(31, 0), Array(
    (io.src2(31, 0) === 0.U) -> io.src1(31, 0)
    ))
  val res_64 = MuxCase(io.src1 % io.src2, Array(
    (io.src2 === 0.U) -> io.src1
    ))

  io.res := Mux(io.word_op, Cat(Fill(32, res_32(31)), res_32(31, 0)), res_64(63, 0))
}

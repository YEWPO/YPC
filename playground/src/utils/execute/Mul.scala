package utils.execute

import chisel3._
import chisel3.util._

class GenPartSummandIO(len: Int) extends Bundle {
  val src     = Input(UInt(len.W))
  val booth   = Input(UInt(3.W))
  val summand = Output(UInt(len.W))
  val carry   = Output(Bool())
}

class GenPartSummand(len: Int) extends Module {
  val io = IO(new GenPartSummandIO(len))

  val src = Cat(io.src, 0.U(1.W))

  val addOnce    = ~io.booth(2) & (io.booth(1) ^ io.booth(0))
  val minusOnce  = io.booth(2) & (io.booth(1) ^ io.booth(0))
  val addTwice   = ~io.booth(2) & io.booth(1) & io.booth(0)
  val minusTwice = io.booth(2) & ~io.booth(1) & ~io.booth(0)

  val summand = Wire(Vec(len, UInt(1.W)))

  for (i <- 0 until len) {
    summand(i) := (addOnce & src(i + 1)) |
      (minusOnce & ~src(i + 1)) |
      (addTwice & src(i)) |
      (minusTwice & ~src(i))
  }

  io.summand := summand.asUInt

  io.carry := io.booth(2) & ~(io.booth(1) & io.booth(0))
}

class GenPartSummandsIO(len: Int) extends Bundle {
  val multiplicand = Input(UInt(len.W))
  val multiplier   = Input(UInt(len.W))

  val summands = Output(Vec(len / 2, UInt((2 * len).W)))
  val carrys   = Output(UInt((len / 2).W))
}

class GenPartSummands(len: Int) extends Module {
  val io = IO(new GenPartSummandsIO(len))

  val multiplicand = Cat(Fill(len, io.multiplicand(len - 1)), io.multiplicand)
  val multiplier   = Cat(io.multiplier, 0.U(1.W))
  val carrys       = Wire(Vec(len / 2, Bool()))

  for (i <- len - 1 to 1 by -2) {
    val genPartSummand = Module(new GenPartSummand(2 * len))

    genPartSummand.io.src   := multiplicand
    genPartSummand.io.booth := multiplier(i + 1, i - 1)

    io.summands((i - 1) / 2) := genPartSummand.io.summand
    carrys((i - 1) / 2)      := genPartSummand.io.carry
  }

  io.carrys := carrys.asUInt
}

class SummandsSwitchIO(len: Int) extends Bundle {
  val summands      = Input(Vec(len / 2, UInt((2 * len).W)))
  val wallaceInputs = Output(Vec(2 * len, UInt((len / 2).W)))
}

class SummandsSwitch(len: Int) extends Module {
  val io = IO(new SummandsSwitchIO(len))

  val wallaceInputs = Wire(Vec(2 * len, Vec(len / 2, UInt(1.W))))

  for (i <- 0 until 2 * len) {
    for (j <- 0 until len / 2) {
      wallaceInputs(i)(j) := io.summands(j)(i)
    }

    io.wallaceInputs(i) := wallaceInputs(i).asUInt
  }
}

class MulIO(len: Int) extends Bundle {
  val multiplicand = Input(UInt(len.W))
  val multiplier   = Input(UInt(len.W))

  val answer = Output(UInt((2 * len).W))
}

class Mul(len: Int = 64) extends Module {
  val io = IO(new MulIO(len))

  // ========== Modules ==========
  val genPartSummands = Module(new GenPartSummands(len))
  val summandsSwitch  = Module(new SummandsSwitch(len))

  genPartSummands.io.multiplicand := io.multiplicand
  genPartSummands.io.multiplier   := io.multiplier

  summandsSwitch.io.summands := genPartSummands.io.summands

  io.answer := DontCare
}

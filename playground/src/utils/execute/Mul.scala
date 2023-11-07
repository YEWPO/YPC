package utils.execute

import chisel3._
import chisel3.util._

class GenPartSummandIO(xlen: Int) extends Bundle {
  val src     = Input(UInt(xlen.W))
  val booth   = Input(UInt(3.W))
  val summand = Output(UInt(xlen.W))
  val carry   = Output(Bool())
}

class GenPartSummand(xlen: Int) extends Module {
  val io = IO(new GenPartSummandIO(xlen))

  val src = Cat(io.src, 0.U(1.W))

  val addOnce    = ~io.booth(2) & (io.booth(1) ^ io.booth(0))
  val minusOnce  = io.booth(2) & (io.booth(1) ^ io.booth(0))
  val addTwice   = ~io.booth(2) & io.booth(1) & io.booth(0)
  val minusTwice = io.booth(2) & ~io.booth(1) & ~io.booth(0)

  val summand = Wire(Vec(xlen, UInt(1.W)))

  for (i <- 0 until xlen) {
    summand(i) := (addOnce & src(i + 1)) |
      (minusOnce & ~src(i + 1)) |
      (addTwice & src(i)) |
      (minusTwice & ~src(i))
  }

  io.summand := summand.asUInt

  io.carry := io.booth(2) & ~(io.booth(1) & io.booth(0))
}

class MulIO(xlen: Int) extends Bundle {
  val src = Input(UInt(xlen.W))
  val out = Output(UInt(xlen.W))
}

class Mul(xlen: Int) extends Module {
  val io = IO(new MulIO(xlen))
}

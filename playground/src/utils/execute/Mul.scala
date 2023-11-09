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

/**
  * ============================
  * for 32 summands
  * ============================
  */
class WallaceTreeIO extends Bundle {
  val inputs   = Input(UInt(32.W))
  val carrysIn = Input(UInt(30.W))

  val output    = Output(UInt(1.W))
  val carryOut  = Output(UInt(1.W))
  val carrysOut = Output(UInt(30.W))
}

class WallaceTree extends Module {
  val io = IO(new WallaceTreeIO)

  def pseudoadder(a: UInt, b: UInt, c: UInt): Tuple2[UInt, UInt] = (
    (!a & !b & c) | (!a & b & !c) | (a & !b & !c) | (a & b & c),
    (a & b) | (b & c) | (a & c)
  )

  val carrysOut      = Wire(Vec(30, UInt(1.W)))
  var carrysInIndex  = 0
  var carrysOutIndex = 0

  /**
    * ============================
    * level 1
    * ============================
    */
  val level1Inputs = Wire(Vec(33, UInt(1.W)))
  for (i <- 0 until 32) {
    level1Inputs(i) := io.inputs(i)
  }
  level1Inputs(32) := 0.U(1.W)
  val level1Outputs =
    for (i <- 0 until 33 by 3) yield pseudoadder(level1Inputs(i), level1Inputs(i + 1), level1Inputs(i + 2))
  for (i <- 0 until 11) {
    carrysOut(carrysOutIndex) := level1Outputs(i)._2
    carrysOutIndex = carrysOutIndex + 1
  }

  /**
    * ============================
    * level 2
    * ============================
    */
  val level2Inputs = Wire(Vec(21, UInt(1.W)))
  for (i <- 0 until 11) {
    level2Inputs(i) := level1Outputs(i)._1
  }
  for (i <- 11 until 21) {
    level2Inputs(i) := io.carrysIn(carrysInIndex)
    carrysInIndex = carrysInIndex + 1
  }
  val level2Outputs =
    for (i <- 0 until 21 by 3) yield pseudoadder(level2Inputs(i), level2Inputs(i + 1), level2Inputs(i + 2))
  for (i <- 0 until 7) {
    carrysOut(carrysOutIndex) := level2Outputs(i)._2
    carrysOutIndex = carrysOutIndex + 1
  }

  /**
    * ============================
    * level 3
    * ============================
    */
  val level3Inputs = Wire(Vec(15, UInt(1.W)))
  for (i <- 0 until 7) {
    level3Inputs(i) := level2Outputs(i)._1
  }
  for (i <- 7 until 15) {
    level3Inputs(i) := io.carrysIn(carrysInIndex)
    carrysInIndex = carrysInIndex + 1
  }
  val level3Outputs =
    for (i <- 0 until 15 by 3) yield pseudoadder(level3Inputs(i), level3Inputs(i + 1), level3Inputs(i + 2))
  for (i <- 0 until 5) {
    carrysOut(carrysOutIndex) := level3Outputs(i)._2
    carrysOutIndex = carrysOutIndex + 1
  }

  /**
    * ============================
    * level 4
    * ============================
    */
  val level4Inputs = Wire(Vec(9, UInt(1.W)))
  for (i <- 0 until 5) {
    level4Inputs(i) := level3Outputs(i)._1
  }
  for (i <- 5 until 9) {
    level4Inputs(i) := io.carrysIn(carrysInIndex)
    carrysInIndex = carrysInIndex + 1
  }
  val level4Outputs =
    for (i <- 0 until 9 by 3) yield pseudoadder(level4Inputs(i), level4Inputs(i + 1), level4Inputs(i + 2))
  for (i <- 0 until 3) {
    carrysOut(carrysOutIndex) := level4Outputs(i)._2
    carrysOutIndex = carrysOutIndex + 1
  }

  /**
    * ============================
    * level 5
    * ============================
    */
  val level5Inputs = Wire(Vec(6, UInt(1.W)))
  for (i <- 0 until 3) {
    level5Inputs(i) := level4Outputs(i)._1
  }
  for (i <- 3 until 6) {
    level5Inputs(i) := io.carrysIn(carrysInIndex)
    carrysInIndex = carrysInIndex + 1
  }
  val level5Outputs =
    for (i <- 0 until 6 by 3) yield pseudoadder(level5Inputs(i), level5Inputs(i + 1), level5Inputs(i + 2))
  for (i <- 0 until 2) {
    carrysOut(carrysOutIndex) := level5Outputs(i)._2
    carrysOutIndex = carrysOutIndex + 1
  }

  /**
    * ============================
    * level 6
    * ============================
    */
  val level6Output = pseudoadder(level5Outputs(0)._1, level5Outputs(1)._1, io.carrysIn(carrysInIndex))
  carrysInIndex = carrysInIndex + 1
  carrysOut(carrysOutIndex) := level6Output._2
  carrysOutIndex = carrysOutIndex + 1

  /**
    * ============================
    * level 7
    * ============================
    */
  val level7Output = pseudoadder(level6Output._1, io.carrysIn(carrysInIndex), io.carrysIn(carrysInIndex + 1))
  carrysInIndex = carrysInIndex + 2
  carrysOut(carrysOutIndex) := level7Output._2
  carrysOutIndex = carrysOutIndex + 1

  /**
    * ============================
    * level 8
    * ============================
    */
  val level8Output = pseudoadder(level7Output._1, io.carrysIn(carrysInIndex), io.carrysIn(carrysInIndex + 1))

  io.output    := level8Output._1
  io.carryOut  := level8Output._2
  io.carrysOut := carrysOut.asUInt
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

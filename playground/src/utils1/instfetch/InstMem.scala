package utils1.instfetch

import chisel3._
import chisel3.util._

class InstMemIO extends Bundle {
  val en     = Input(Bool())
  val addr   = Input(UInt(64.W))
  val r_data = Output(UInt(64.W))
}

class InstMem extends BlackBox with HasBlackBoxPath {
  val io = IO(new InstMemIO)

  addPath("playground/src/utils/InstMem.v")
}

package utils

import chisel3._

class InstMemIO extends Bundle {
  val en   = Input(Bool())
  val addr = Input(UInt(64.W))
  val inst = Output(UInt(32.W))
}

class InstMem extends Module {
  val io = IO(new InstMemIO)

  val mem_read = Module(new MemRead)

  mem_read.io.addr := io.addr
  mem_read.io.r_en := io.en
  io.inst          := Mux(io.addr(2).orR, mem_read.io.r_data(63, 32), mem_read.io.r_data(31, 0))
}

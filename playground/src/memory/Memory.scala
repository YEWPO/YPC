package memory

import chisel3._
import chisel3.util._

class Memory extends Module {
  val io = IO(new Bundle {
    val addr = Input(UInt(64.W))
    val w_data = Input(UInt(64.W))
    val op = Input(Bool())
    val m_en = Input(Bool())
    val len = Input(UInt(32.W))
    val funct = Input(UInt(3.W))

    val r_data = Output(UInt(64.W))
  })

  val memoryio = Module(new Memoryio)

  memoryio.io.addr := io.addr
  memoryio.io.w_data := io.w_data
  memoryio.io.op := io.op
  memoryio.io.m_en := io.m_en
  memoryio.io.len := io.len

  io.r_data := MuxCase(memoryio.io.r_data, Array (
    (io.funct === "b000".U) -> Cat(Fill(56, memoryio.io.r_data(7)), memoryio.io.r_data(7, 0)),
    (io.funct === "b001".U) -> Cat(Fill(48, memoryio.io.r_data(15)), memoryio.io.r_data(15, 0)),
    (io.funct === "b010".U) -> Cat(Fill(32, memoryio.io.r_data(31)), memoryio.io.r_data(31, 0))
    ))
}

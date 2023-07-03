import chisel3._
import chisel3.util._

import register._

class Top extends Module {
  val io = IO(new Bundle{
    val inst = Input(UInt(32.W))
    val pc = Output(UInt(64.W))
  })

  val program_counter = Module(new PC)

  io.pc := program_counter.io.pc
}

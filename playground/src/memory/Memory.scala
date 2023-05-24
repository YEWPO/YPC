package memory

import chisel3._
import chisel3.util.HasBlackBoxResource

class Memory extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val addr = Input(UInt(64.W))
    val w_data = Input(UInt(64.W))
    val op = Input(Bool())
    val len = Input(UInt(32.W))
    val r_data = Output(UInt(64.W))
  })

  addResource("Memory.v")
}

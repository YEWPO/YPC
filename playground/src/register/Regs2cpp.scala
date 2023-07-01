package register

import chisel3._
import chisel3.util.HasBlackBoxResource

class Regs2cpp extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val inbits = Input(UInt((64*32).W))
  })

  addResource("Regs2cpp.v")
}

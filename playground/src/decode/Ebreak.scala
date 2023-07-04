package decode

import chisel3._
import chisel3.util.HasBlackBoxResource

class Ebreak extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val inst = Input(UInt(32.W))
  })

  addResource("Ebreak.v")
}

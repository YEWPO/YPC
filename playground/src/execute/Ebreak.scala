package execute

import chisel3._
import chisel3.util.HasBlackBoxResource

class Ebreak extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val op_type = Input(UInt(3.W))
  })

  addResource("Ebreak.v")
}

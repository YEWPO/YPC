package register

import chisel3._
import chisel3.util.HasBlackBoxResource

class PC2cpp extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val pc_val = Input(UInt(64.W))
  })

  addResource("PC2cpp.v")
}

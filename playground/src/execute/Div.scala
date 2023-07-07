package execute

import chisel3._
import chisel3.util._

class Div extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))
    val res_32 = Output(UInt(32.W))
    val res_64 = Output(UInt(64.W))
  })

  addResource("Div.v")
}

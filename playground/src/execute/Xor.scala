package execute

import chisel3._

class Xor extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))
    val res = Output(UInt(64.W))
  })

  io.res := io.src1 ^ io.src2
}

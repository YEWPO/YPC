package execute

import chisel3._

class Equal extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))
    val res = Output(Bool())
  })

  io.res := Mux(io.src1 === io.src2, true.B, false.B)
}

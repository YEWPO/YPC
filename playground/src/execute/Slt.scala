package execute

import chisel3._

class Slt extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))
    val res = Output(UInt(64.W))
  })

  io.res := Mux(io.src1.asSInt < io.src2.asSInt, 1.U(64.W), 0.U(64.W))
}

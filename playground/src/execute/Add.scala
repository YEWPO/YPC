package execute

import chisel3._
import chisel3.util._

class Add extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))
    val add_op = Input(Bool())
    val res = Output(UInt(64.W))
  })

  val add_val = io.src1 +& io.src2
  val minus_val = io.src1 -& io.src2

  io.res := Mux(io.add_op, minus_val, add_val)
}

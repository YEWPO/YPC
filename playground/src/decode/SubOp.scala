package decode

import chisel3._

class SubOp extends Module {
  val io = IO(new Bundle {
    val inst = Input(UInt(64.W))
    val sub_op = Output(Bool())
  })

  val sub_op = (
    (io.inst(6, 0) === "b0010011".U && io.inst(14, 12) === "b101".U)
    || (io.inst(6, 0) === "b0110011".U && ((io.inst(14, 12) === "b000".U) || (io.inst(14, 12) === "b101".U)))
    || (io.inst(6, 0) === "b0011011".U && io.inst(14, 12) === "b101".U)
    || (io.inst(6, 0) === "b0111011".U && ((io.inst(14, 12) === "b000".U) || (io.inst(14, 12) === "b101".U)))
  )

  io.sub_op := Mux(sub_op, (io.inst(30) === 1.U), false.B)
}

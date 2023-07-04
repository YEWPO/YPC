package register

import chisel3._

class Snpc extends Module {
  val io = IO(new Bundle {
    val opcode = Input(UInt(2.W))
    val pc = Input(UInt(64.W))

    val snpc = Output(UInt(64.W))
  })

  io.snpc := Mux(io.opcode === "b11".U, io.pc + 4.U(64.W), io.pc)
}

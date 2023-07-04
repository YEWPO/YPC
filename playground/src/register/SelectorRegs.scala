package register

import chisel3._

class SelectorRegs extends Module {
  val io = IO(new Bundle {
    val opcode = Input(UInt(64.W))
    val snpc = Input(UInt(64.W))
    val alu_out = Input(UInt(64.W))

    val data_out = Output(UInt(64.W))
  })

  val op_j = (io.opcode === "b1101111".U) || (io.opcode === "b1100111".U)

  io.data_out := Mux(op_j, io.snpc, io.alu_out)
}

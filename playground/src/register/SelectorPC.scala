package register

import chisel3._
import chisel3.util._

class SelectorPC extends Module {
  val io = IO(new Bundle {
    val opcode = Input(UInt(7.W))
    val branch_out = Input(Bool())
    val snpc = Input(UInt(64.W))
    val alu_out = Input(UInt(64.W))

    val next_pc = Output(UInt(64.W))
  })

  val op_j = (io.opcode === "b1101111".U) || (io.opcode === "b1100111".U)

  val dnpc = Cat(io.alu_out(63, 1), 0.U(1.W))

  io.next_pc := Mux(io.branch_out || op_j, dnpc, io.snpc)
}

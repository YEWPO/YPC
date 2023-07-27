package unit

import chisel3._
import utils._
import chisel3.util._

class InstFetchUnitIO extends Bundle {
  val enable = Input(Bool())

  val npc = Input(UInt(64.W))

  val pc_f   = Output(UInt(64.W))
  val inst_f = Output(UInt(32.W))
  val snpc_f = Output(UInt(64.W))
}

class InstFetchUnit extends Module {
  val io = IO(new InstFetchUnitIO)

  /**
    * pc = npc
    */
  val program_counter = RegEnable(io.npc, "h8000_0000".U(64.W), io.enable)
  val inst_mem        = Module(new InstMem)

  inst_mem.io.addr := program_counter

  /**
    * inst = mem[pc]
    * snpc = pc + 4
    */
  io.inst_f := inst_mem.io.inst
  io.pc_f   := program_counter
  io.snpc_f := program_counter + 4.U
}

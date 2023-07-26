package unit

import chisel3._
import utils._

class InstFetchUnitIO extends Bundle {
  val npc = Input(UInt(64.W))

  val pc_f = Output(UInt(64.W))
  val inst = Output(UInt(32.W))
  val snpc = Output(UInt(64.W))
}

class InstFetchUnit extends Module {
  val io = IO(new InstFetchUnitIO())

  val program_counter = RegInit("h8000_0000".U)
  val inst_mem = Module(new InstMem())

  /**
    * pc = npc
    */
  program_counter := io.npc

  /**
    * inst = mem[pc]
    * snpc = pc + 4
    */
  inst_mem.io.addr := program_counter
  io.inst := inst_mem.io.inst
  io.pc_f := program_counter
  io.snpc := program_counter + 4.U
}

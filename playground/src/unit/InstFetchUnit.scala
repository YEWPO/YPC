package unit

import chisel3._
import utils._
import chisel3.util._

/**
  * input:
  * npc
  *
  * output:
  * inst, snpc, pc
  */
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
  val program_counter = RegEnable(Cat(io.npc(63, 1), 0.U(1.W)), CommonMacro.PC_RESET_VAL, io.enable)
  val inst_mem        = Module(new InstMem)

  /**
    * instruction memory
    */
  inst_mem.io.addr := program_counter

  /**
    * inst = mem[pc]
    * snpc = pc + 4
    */
  io.inst_f := inst_mem.io.inst
  io.pc_f   := program_counter
  io.snpc_f := program_counter + 4.U
}

package unit

import chisel3._
import utils._
import chisel3.util._
import entity._

class InstFetchUnitIO extends Bundle {
  val npc = Input(UInt(64.W))
}

class InstFetchUnit extends Module {
  val npc               = IO(Input(UInt(64.W)))
  val inst_fetch_data   = IO(new InstFetchData)
  val inst_fetch_hazard = IO(new InstFetchHazard)

  /**
    * pc = npc
    */
  val program_counter = RegEnable(Cat(npc(63, 1), 0.U(1.W)), CommonMacro.PC_RESET_VAL, inst_fetch_hazard.enable)
  val inst_mem        = Module(new InstMem)

  /**
    * instruction memory
    */
  inst_mem.io.addr := program_counter

  /**
    * inst = mem[pc]
    * snpc = pc + 4
    */
  inst_fetch_data.inst := inst_mem.io.inst
  inst_fetch_data.pc   := program_counter
  inst_fetch_data.snpc := program_counter + 4.U
}

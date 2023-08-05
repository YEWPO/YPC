package unit

import chisel3._
import entity._
import utils._

class LoadStoreUnit extends Module {
  val execute_data       = IO(Flipped(new ExecuteData))
  val execute_control    = IO(Flipped(new ExecuteControl))
  val load_store_data    = IO(new LoadStoreData)
  val load_store_control = IO(new LoadStoreControl)
  val load_store_hazard  = IO(new LoadStoreHazard)
  val load_store_forward = IO(new LoadStoreForward)

  val data_mem = Module(new DataMem)

  // data registers
  val snpc    = RegNext(execute_data.snpc, CommonMacro.PC_RESET_VAL)
  val pc      = RegNext(execute_data.pc, CommonMacro.PC_RESET_VAL)
  val inst    = RegNext(execute_data.inst, CommonMacro.INST_RESET_VAL)
  val rd      = RegNext(execute_data.rd, 0.U)
  val src2    = RegNext(execute_data.src2, 0.U)
  val exe_out = RegNext(execute_data.exe_out, 0.U)
}

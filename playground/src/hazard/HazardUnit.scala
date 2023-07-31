package hazard

import chisel3._
import entity._
import control._

class HazardUnit extends Module {
  val inst_fetch_hazard  = Flipped(IO(new InstFetchHazard))
  val inst_decode_hazard = Flipped(IO(new InstDecodeHazard))
  val execute_hazard     = Flipped(IO(new ExecuteHazard))
  val load_store_hazard  = Flipped(IO(new LoadStoreHazard))
  val write_back_hazard  = Flipped(IO(new WriteBackHazard))

  val not_execute_wb_mem     = execute_hazard.wb_ctl =/= ControlMacro.WB_CTL_MEM
  val rs1_not_hazard_execute = inst_decode_hazard.rs1 =/= execute_hazard.rd || inst_decode_hazard.rs1 === 0.U
  val rs2_not_hazard_execute = inst_decode_hazard.rs2 =/= execute_hazard.rd || inst_decode_hazard.rs2 === 0.U
  val not_execute_mem_out    = not_execute_wb_mem || (rs1_not_hazard_execute && rs2_not_hazard_execute)

  inst_fetch_hazard.enable  := not_execute_mem_out
  inst_decode_hazard.enable := not_execute_mem_out
  inst_decode_hazard.reset  := execute_hazard.jump_sig
  execute_hazard.reset      := execute_hazard.jump_sig || !not_execute_mem_out
}

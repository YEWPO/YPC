package hazard

import chisel3._
import entity._
import control._
import chisel3.util._

class HazardUnit extends Module {
  val inst_fetch_hazard  = IO(Flipped(new InstFetchHazard))
  val inst_decode_hazard = IO(Flipped(new InstDecodeHazard))
  val execute_hazard     = IO(Flipped(new ExecuteHazard))
  val load_store_hazard  = IO(Flipped(new LoadStoreHazard))
  val write_back_hazard  = IO(Flipped(new WriteBackHazard))

  val not_execute_wb_mem     = execute_hazard.wb_ctl =/= ControlMacro.WB_CTL_MEM
  val rs1_not_hazard_execute = inst_decode_hazard.rs1 =/= execute_hazard.rd || inst_decode_hazard.rs1 === 0.U
  val rs2_not_hazard_execute = inst_decode_hazard.rs2 =/= execute_hazard.rd || inst_decode_hazard.rs2 === 0.U
  val not_execute_mem_out    = not_execute_wb_mem || (rs1_not_hazard_execute && rs2_not_hazard_execute)

  inst_fetch_hazard.enable  := not_execute_mem_out
  inst_decode_hazard.enable := not_execute_mem_out
  inst_decode_hazard.reset  := execute_hazard.jump_sig
  execute_hazard.reset      := execute_hazard.jump_sig || !not_execute_mem_out

  def get_fw_rules(rs: UInt) = Seq(
    (rs === execute_hazard.rd && execute_hazard.wb_ctl === ControlMacro.WB_CTL_ALU) -> HazardMacro.F_CTL_ALU_OUT_E,
    (rs === execute_hazard.rd && execute_hazard.wb_ctl === ControlMacro.WB_CTL_SNPC) -> HazardMacro.F_CTL_SNPC_E,
    (rs === load_store_hazard.rd && execute_hazard.wb_ctl === ControlMacro.WB_CTL_ALU) -> HazardMacro.F_CTL_ALU_OUT_M,
    (rs === load_store_hazard.rd && execute_hazard.wb_ctl === ControlMacro.WB_CTL_MEM) -> HazardMacro.F_CTL_MEM_OUT_M,
    (rs === load_store_hazard.rd && execute_hazard.wb_ctl === ControlMacro.WB_CTL_SNPC) -> HazardMacro.F_CTL_SNPC_M,
    (rs === write_back_hazard.rd) -> HazardMacro.F_CTL_WB_DATA,
    true.B -> HazardMacro.F_CTL_DEFAULT
  )

  inst_decode_hazard.fa_ctl := PriorityMux(get_fw_rules(inst_decode_hazard.rs1))
  inst_decode_hazard.fb_ctl := PriorityMux(get_fw_rules(inst_decode_hazard.rs2))
}

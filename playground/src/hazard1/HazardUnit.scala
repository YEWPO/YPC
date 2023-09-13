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

  val not_execute_wb_mem = execute_hazard.wb_ctl =/= ControlMacro.WB_CTL_MEM || !execute_hazard.rd_tag
  val rs1_not_hazard_execute =
    inst_decode_hazard.rs1 =/= execute_hazard.rd || !inst_decode_hazard.rs1.orR || !inst_decode_hazard.rs1_tag
  val rs2_not_hazard_execute =
    inst_decode_hazard.rs2 =/= execute_hazard.rd || !inst_decode_hazard.rs2.orR || !inst_decode_hazard.rs2_tag
  val not_execute_mem_out = not_execute_wb_mem || (rs1_not_hazard_execute && rs2_not_hazard_execute)

  inst_fetch_hazard.enable  := not_execute_mem_out
  inst_decode_hazard.enable := not_execute_mem_out
  inst_decode_hazard.reset  := execute_hazard.jump_sig
  execute_hazard.reset      := execute_hazard.jump_sig || !not_execute_mem_out

  /**
    * rs != 0 && rs hazard
    */
  def get_fw_rules(rs: UInt) = Seq(
    (rs.orR && execute_hazard.rd_tag && rs === execute_hazard.rd && execute_hazard.wb_ctl === ControlMacro.WB_CTL_ALU)           -> HazardMacro.F_CTL_ALU_E,
    (rs.orR && execute_hazard.rd_tag && rs === execute_hazard.rd && execute_hazard.wb_ctl === ControlMacro.WB_CTL_MEM)           -> HazardMacro.F_CTL_DEFAULT,
    (rs.orR && execute_hazard.rd_tag && rs === execute_hazard.rd && execute_hazard.wb_ctl === ControlMacro.WB_CTL_SNPC)          -> HazardMacro.F_CTL_SNPC_E,
    (rs.orR && load_store_hazard.rd_tag && rs === load_store_hazard.rd && load_store_hazard.wb_ctl === ControlMacro.WB_CTL_ALU)  -> HazardMacro.F_CTL_ALU_M,
    (rs.orR && load_store_hazard.rd_tag && rs === load_store_hazard.rd && load_store_hazard.wb_ctl === ControlMacro.WB_CTL_MEM)  -> HazardMacro.F_CTL_MEM_M,
    (rs.orR && load_store_hazard.rd_tag && rs === load_store_hazard.rd && load_store_hazard.wb_ctl === ControlMacro.WB_CTL_SNPC) -> HazardMacro.F_CTL_SNPC_M,
    (rs.orR && write_back_hazard.rd_tag && rs === write_back_hazard.rd)                                                          -> HazardMacro.F_CTL_WB_DATA,
    true.B                                                                                                                       -> HazardMacro.F_CTL_DEFAULT
  )

  inst_decode_hazard.fa_ctl := Mux(
    inst_decode_hazard.rs1_tag,
    PriorityMux(get_fw_rules(inst_decode_hazard.rs1)),
    HazardMacro.F_CTL_DEFAULT
  )
  inst_decode_hazard.fb_ctl := Mux(
    inst_decode_hazard.rs2_tag,
    PriorityMux(get_fw_rules(inst_decode_hazard.rs2)),
    HazardMacro.F_CTL_DEFAULT
  )
}
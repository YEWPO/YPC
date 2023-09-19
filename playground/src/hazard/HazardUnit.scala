package hazard

import chisel3._
import chisel3.util._
import macros._
import bundles._
import bundles.instdecode._
import bundles.execute._
import bundles.loadstore._
import bundles.writeback._

class HazardUnitIO extends Bundle {
  val inst_decode = new Bundle {
    val data    = Input(new IDHazardDataBundle)
    val control = Output(new IDHazardControlBundle)
  }
  val execute = new Bundle {
    val data    = Input(new EXHazardDataBundle)
    val control = Input(new EXHazardControlBundle)
  }
  val load_store = new Bundle {
    val data    = Input(new LSHazardDataBundle)
    val control = Input(new LSHazardControlBundle)
  }
  val write_back = new Bundle {
    val data    = Input(new WBHazardDataBundle)
    val control = Input(new WBHazardControlBundle)
  }

  val csr_reset      = Input(Bool())
  val ifu_inst_valid = Input(Bool())

  val pc_enable     = Output(Bool())
  val if_id_control = Output(new StageControlBundle)
  val id_ex_control = Output(new StageControlBundle)
  val ex_ls_control = Output(new StageControlBundle)
  val ls_wb_control = Output(new StageControlBundle)
}

class HazardUnit extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new HazardUnitIO)

  /* ========== Wire ========== */
  val not_execute_wb_mem = io.execute.data.wb_ctl =/= ControlMacros.WB_CTL_MEM || !io.execute.data.rd_tag
  val rs1_not_hazard_execute =
    io.inst_decode.data.rs1 =/= io.execute.data.rd || !io.inst_decode.data.rs1.orR || !io.inst_decode.data.rs1_tag
  val rs2_not_hazard_execute =
    io.inst_decode.data.rs2 =/= io.execute.data.rd || !io.inst_decode.data.rs2.orR || !io.inst_decode.data.rs2_tag
  val not_execute_mem_out = not_execute_wb_mem || (rs1_not_hazard_execute && rs2_not_hazard_execute)

  /* ========== Function ========== */
  def get_fw_rules(rs: UInt) = Seq(
    (rs.orR && io.execute.data.rd_tag && rs === io.execute.data.rd && io.execute.data.wb_ctl === ControlMacros.WB_CTL_ALU)           -> HazardMacros.F_CTL_EXE_E,
    (rs.orR && io.execute.data.rd_tag && rs === io.execute.data.rd && io.execute.data.wb_ctl === ControlMacros.WB_CTL_MEM)           -> HazardMacros.F_CTL_DEFAULT,
    (rs.orR && io.execute.data.rd_tag && rs === io.execute.data.rd && io.execute.data.wb_ctl === ControlMacros.WB_CTL_SNPC)          -> HazardMacros.F_CTL_SNPC_E,
    (rs.orR && io.load_store.data.rd_tag && rs === io.load_store.data.rd && io.load_store.data.wb_ctl === ControlMacros.WB_CTL_ALU)  -> HazardMacros.F_CTL_EXE_M,
    (rs.orR && io.load_store.data.rd_tag && rs === io.load_store.data.rd && io.load_store.data.wb_ctl === ControlMacros.WB_CTL_MEM)  -> HazardMacros.F_CTL_MEM_M,
    (rs.orR && io.load_store.data.rd_tag && rs === io.load_store.data.rd && io.load_store.data.wb_ctl === ControlMacros.WB_CTL_SNPC) -> HazardMacros.F_CTL_SNPC_M,
    (rs.orR && io.write_back.data.rd_tag && rs === io.write_back.data.rd)                                                            -> HazardMacros.F_CTL_WB_DATA,
    true.B                                                                                                                           -> HazardMacros.F_CTL_DEFAULT
  )

  /* ========== Combinational Circuit ========== */
  io.pc_enable            := not_execute_mem_out
  io.if_id_control.enable := not_execute_mem_out && io.ifu_inst_valid
  io.if_id_control.reset  := reset.asBool || io.execute.data.jump_sig || io.csr_reset
  io.id_ex_control.enable := true.B && io.ifu_inst_valid
  io.id_ex_control.reset  := reset.asBool || io.execute.data.jump_sig || !not_execute_mem_out
  io.ex_ls_control.enable := true.B && io.ifu_inst_valid
  io.ex_ls_control.reset  := reset.asBool
  io.ls_wb_control.enable := true.B && io.ifu_inst_valid
  io.ls_wb_control.reset  := reset.asBool

  io.inst_decode.control.fa_ctl := Mux(
    io.inst_decode.data.rs1_tag,
    PriorityMux(get_fw_rules(io.inst_decode.data.rs1)),
    HazardMacros.F_CTL_DEFAULT
  )
  io.inst_decode.control.fb_ctl := Mux(
    io.inst_decode.data.rs2_tag,
    PriorityMux(get_fw_rules(io.inst_decode.data.rs2)),
    HazardMacros.F_CTL_DEFAULT
  )
}

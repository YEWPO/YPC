package stages

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import bundles._
import macros._
import bundles.writeback._
import utils.writeback._

class WBUIO extends Bundle {
  val ls2wb = Flipped(Decoupled(new LS2WBBundle))
  val out = Output(new Bundle {
    val data       = Output(new WB2RegBundle)
    val hazard     = Output(new WBHazardDataBundle)
    val csr_hazard = Output(new WBCSRHazardDataBundle)
  })
}

class WBU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new WBUIO)

  /* ========== Module ========== */
  val statistic = Module(new Statistic)

  /* ========== Parameter ========== */
  val ls2wb_rst_val = (new LS2WBBundle).Lit(
    _.data -> (new LS2WBDataBundle).Lit(
      _.snpc       -> CommonMacros.PC_RESET_VAL,
      _.pc         -> CommonMacros.PC_RESET_VAL,
      _.dnpc       -> CommonMacros.PC_RESET_VAL,
      _.inst       -> CommonMacros.INST_RESET_VAL,
      _.rd         -> 0.U,
      _.mem_out    -> 0.U,
      _.exe_out    -> 0.U,
      _.csr_w_data -> 0.U,
      _.csr_w_addr -> 0.U
    ),
    _.control -> (new LS2WBControlBundle).Lit(
      _.wb_ctl     -> ControlMacros.WB_CTL_DEFAULT,
      _.reg_w_en   -> ControlMacros.REG_W_DISABLE,
      _.ebreak_op  -> ControlMacros.EBREAK_OP_NO,
      _.invalid_op -> ControlMacros.INVALID_OP_NO,
      _.csr_w_en   -> false.B
    )
  )

  /* ========== Wire ========== */
  val ready_next = io.ls2wb.valid
  val ls2wb_data = Wire(new LS2WBBundle)

  /* ========== Register ========== */
  val r_statistic = RegInit(
    (new StatisticBundle).Lit(
      _.pc         -> CommonMacros.PC_RESET_VAL,
      _.dnpc       -> CommonMacros.PC_RESET_VAL,
      _.inst       -> CommonMacros.INST_RESET_VAL,
      _.ebreak_op  -> ControlMacros.EBREAK_OP_NO,
      _.invalid_op -> ControlMacros.INVALID_OP_NO
    )
  )

  /* ========== Sequential Circuit ========== */
  r_statistic.ebreak_op  := ls2wb_data.control.ebreak_op
  r_statistic.invalid_op := ls2wb_data.control.invalid_op
  r_statistic.pc         := ls2wb_data.data.pc
  r_statistic.dnpc       := ls2wb_data.data.dnpc
  r_statistic.inst       := ls2wb_data.data.inst

  /* ========== Combinational Circuit ========== */
  ls2wb_data := Mux(io.ls2wb.valid, io.ls2wb.bits, ls2wb_rst_val)

  io.ls2wb.ready := ready_next

  statistic.io.in := r_statistic

  io.out.data.wb_data := MuxLookup(ls2wb_data.control.wb_ctl, 0.U(64.W))(
    Seq(
      ControlMacros.WB_CTL_ALU  -> ls2wb_data.data.exe_out,
      ControlMacros.WB_CTL_MEM  -> ls2wb_data.data.mem_out,
      ControlMacros.WB_CTL_SNPC -> ls2wb_data.data.snpc
    )
  )
  io.out.data.rd       := ls2wb_data.data.rd
  io.out.data.reg_w_en := ls2wb_data.control.reg_w_en

  io.out.data.csr_w_addr := ls2wb_data.data.csr_w_addr
  io.out.data.csr_w_data := ls2wb_data.data.csr_w_data
  io.out.data.csr_w_en   := ls2wb_data.control.csr_w_en

  io.out.hazard.rd                 := ls2wb_data.data.rd
  io.out.hazard.rd_tag             := ls2wb_data.control.reg_w_en
  io.out.hazard.wb_ctl             := ls2wb_data.control.wb_ctl
  io.out.csr_hazard.csr_w_addr     := ls2wb_data.data.csr_w_addr
  io.out.csr_hazard.csr_w_addr_tag := ls2wb_data.control.csr_w_en
}

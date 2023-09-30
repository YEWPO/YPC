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

  val in = Input(new Bundle {
    val tvec = UInt(64.W)
  })
  val out = Output(new Bundle {
    val wb_data = Output(new WB2RegBundle)
    val state_info = Output(new Bundle {
      val rd         = UInt(5.W)
      val reg_w_data = UInt(64.W)
      val csr_w_addr = UInt(12.W)
      val csr_w_data = UInt(64.W)
    })
    val cause = UInt(64.W)
    val epc   = UInt(64.W)
  })
}

class WBU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new WBUIO)
  /* ========== Module ========== */
  val statistic = Module(new Statistic)

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
  r_statistic.dnpc       := Mux(ls2wb_data.data.cause === CommonMacros.CAUSE_RESET_VAL, ls2wb_data.data.dnpc, io.in.tvec)
  r_statistic.inst       := ls2wb_data.data.inst

  /* ========== Combinational Circuit ========== */
  ls2wb_data := Mux(io.ls2wb.valid, io.ls2wb.bits, LS2WBBundle.ls2wb_rst_val)

  io.ls2wb.ready := ready_next

  statistic.io.in := r_statistic

  io.out.wb_data.reg_w_data := ls2wb_data.data.lsu_out
  io.out.wb_data.rd         := ls2wb_data.data.rd
  io.out.wb_data.reg_w_en   := ls2wb_data.control.reg_w_en

  io.out.wb_data.csr_w_addr := ls2wb_data.data.csr_w_addr
  io.out.wb_data.csr_w_data := ls2wb_data.data.csr_w_data
  io.out.wb_data.csr_w_en   := ls2wb_data.control.csr_w_en

  io.out.state_info.rd         := Mux(ls2wb_data.control.reg_w_en, ls2wb_data.data.rd, 0.U(5.W))
  io.out.state_info.reg_w_data := ls2wb_data.data.lsu_out
  io.out.state_info.csr_w_addr := Mux(ls2wb_data.control.csr_w_en, ls2wb_data.data.csr_w_addr, 0.U(12.W))
  io.out.state_info.csr_w_data := ls2wb_data.data.csr_w_data
  io.out.cause                 := ls2wb_data.data.cause
  io.out.epc                   := ls2wb_data.data.pc
}

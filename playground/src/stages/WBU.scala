package stages

import chisel3._
import chisel3.util._
import bundles._
import macros._
import bundles.writeback._
import utils.writeback._

class WBUIO extends Bundle {
  val in = Input(new Bundle {
    val data    = new LS2WBDataBundle
    val control = new LS2WBControlBundle
  })
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

  /* ========== Register ========== */
  val r_statistic = RegInit(0.U.asTypeOf(new StatisticBundle))

  /* ========== Sequential Circuit ========== */
  r_statistic.ebreak_op  := io.in.control.ebreak_op
  r_statistic.invalid_op := io.in.control.invalid_op
  r_statistic.pc         := io.in.data.pc
  r_statistic.dnpc       := io.in.data.dnpc
  r_statistic.inst       := io.in.data.inst

  /* ========== Combinational Circuit ========== */
  statistic.io.in := r_statistic

  io.out.data.wb_data := MuxLookup(io.in.control.wb_ctl, 0.U(64.W))(
    Seq(
      ControlMacros.WB_CTL_ALU  -> io.in.data.exe_out,
      ControlMacros.WB_CTL_MEM  -> io.in.data.mem_out,
      ControlMacros.WB_CTL_SNPC -> io.in.data.snpc
    )
  )
  io.out.data.rd       := io.in.data.rd
  io.out.data.reg_w_en := io.in.control.reg_w_en

  io.out.data.csr_w_addr := io.in.data.csr_w_addr
  io.out.data.csr_w_data := io.in.data.csr_w_data
  io.out.data.csr_w_en   := io.in.control.csr_w_en

  io.out.hazard.rd                 := io.in.data.rd
  io.out.hazard.rd_tag             := io.in.control.reg_w_en
  io.out.hazard.wb_ctl             := io.in.control.wb_ctl
  io.out.csr_hazard.csr_w_addr     := io.in.data.csr_w_addr
  io.out.csr_hazard.csr_w_addr_tag := io.in.control.csr_w_en

  statistic.io.in := r_statistic
}

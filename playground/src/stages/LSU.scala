package stages

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import macros._
import bundles._
import utils.loadstore._

class LSUIO extends Bundle {
  val ex2ls = Flipped(Decoupled(new EX2LSBundle))
  val ls2wb = Decoupled(new LS2WBBundle)

  val in = Input(new Bundle {
    val tvec = UInt(64.W)
  })
  val out = Output(new Bundle {
    val state_info = Output(new Bundle {
      val rd         = UInt(5.W)
      val reg_w_data = UInt(64.W)
      val csr_w_addr = UInt(12.W)
      val csr_w_data = UInt(64.W)
      val mem_r_op   = Bool()
    })
    val cause = UInt(64.W)
    val epc   = UInt(64.W)
  })
}

class LSU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new LSUIO)

  /* ========== Module ========== */
  val data_mem = Module(new DataMem)

  /* ========== Register ========== */
  val r_valid = RegInit(false.B)
  val r_ls2wb = RegInit(LS2WBBundle.ls2wb_rst_val)

  val r_dnpc = RegInit(CommonMacros.PC_RESET_VAL)

  /* ========== Wire ========== */
  val ready_next    = io.ex2ls.valid && (!io.ls2wb.valid || io.ls2wb.ready)
  val valid_enable  = io.ex2ls.valid && (!io.ls2wb.valid || io.ls2wb.ready)
  val valid_next    = r_valid && !io.ls2wb.fire
  val valid_current = io.ex2ls.valid && (r_dnpc === io.ex2ls.bits.data.pc)
  val ex2ls_data    = Wire(new EX2LSBundle)

  val lsu_out = Mux(ex2ls_data.control.mem_ctl(3).orR, data_mem.io.r_data, ex2ls_data.data.exu_out)

  val dnpc_enable = (r_dnpc === io.ex2ls.bits.data.pc) && ready_next
  val dnpc        = Mux(ex2ls_data.data.cause =/= CommonMacros.CAUSE_RESET_VAL, io.in.tvec, ex2ls_data.data.dnpc)

  /* ========== Sequential Circuit ========== */
  r_valid := Mux(valid_enable, io.ex2ls.valid, valid_next)

  r_dnpc := Mux(dnpc_enable, dnpc, r_dnpc)

  r_ls2wb.data.pc            := ex2ls_data.data.pc
  r_ls2wb.data.dnpc          := dnpc
  r_ls2wb.data.inst          := ex2ls_data.data.inst
  r_ls2wb.data.rd            := ex2ls_data.data.rd
  r_ls2wb.data.lsu_out       := lsu_out
  r_ls2wb.control.reg_w_en   := ex2ls_data.control.reg_w_en
  r_ls2wb.control.ebreak_op  := ex2ls_data.control.ebreak_op
  r_ls2wb.control.invalid_op := ex2ls_data.control.invalid_op

  r_ls2wb.data.csr_w_addr  := ex2ls_data.data.csr_w_addr
  r_ls2wb.data.csr_w_data  := ex2ls_data.data.csr_w_data
  r_ls2wb.control.csr_w_en := ex2ls_data.control.csr_w_en

  /* ========== Combinational Circuit ========== */
  io.ex2ls.ready := ready_next
  io.ls2wb.valid := r_valid

  io.ls2wb.bits := r_ls2wb

  ex2ls_data := Mux(valid_current, io.ex2ls.bits, EX2LSBundle.ex2ls_rst_val)

  data_mem.io.addr    := ex2ls_data.data.exu_out
  data_mem.io.w_data  := ex2ls_data.data.src2
  data_mem.io.mem_ctl := ex2ls_data.control.mem_ctl

  io.out.state_info.rd         := Mux(ex2ls_data.control.reg_w_en, ex2ls_data.data.rd, 0.U(5.W))
  io.out.state_info.reg_w_data := lsu_out
  io.out.state_info.csr_w_addr := Mux(ex2ls_data.control.csr_w_en, ex2ls_data.data.csr_w_addr, 0.U(12.W))
  io.out.state_info.csr_w_data := ex2ls_data.data.csr_w_data
  io.out.state_info.mem_r_op   := ex2ls_data.control.mem_ctl(3)

  io.out.cause := ex2ls_data.data.cause
  io.out.epc   := ex2ls_data.data.pc
}

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
    val cause = UInt(64.W)
  })
  val out = Output(new Bundle {
    val state_info = Output(new Bundle {
      val rd         = UInt(5.W)
      val reg_w_data = UInt(64.W)
      val csr_w_addr = UInt(12.W)
      val csr_w_data = UInt(64.W)
      val mem_r_op   = Bool()
    })
  })
}

class LSU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new LSUIO)

  /* ========== Module ========== */
  val ls_handler = Module(new LSHandler)

  /* ========== Register ========== */
  val r_valid = RegInit(false.B)
  val r_ls2wb = RegInit(LS2WBBundle.ls2wb_rst_val)

  /* ========== Wire ========== */
  val ready_next   = io.ex2ls.valid && (!io.ls2wb.valid || io.ls2wb.ready) && ls_handler.io.fin
  val valid_enable = io.ex2ls.valid && (!io.ls2wb.valid || io.ls2wb.ready) && ls_handler.io.fin
  val valid_next   = r_valid && !io.ls2wb.fire
  val ex2ls_data   = Wire(new EX2LSBundle)
  val valid_current =
    io.ex2ls.valid && (io.in.cause === CommonMacros.CAUSE_RESET_VAL)

  val lsu_out = Mux(ex2ls_data.control.mem_ctl(3).orR, ls_handler.io.r_data, ex2ls_data.data.exu_out)

  /* ========== Sequential Circuit ========== */
  r_valid := Mux(valid_enable, io.ex2ls.valid, valid_next)

  r_ls2wb.data.pc            := Mux(valid_enable, ex2ls_data.data.pc, r_ls2wb.data.pc)
  r_ls2wb.data.dnpc          := Mux(valid_enable, ex2ls_data.data.dnpc, r_ls2wb.data.dnpc)
  r_ls2wb.data.inst          := Mux(valid_enable, ex2ls_data.data.inst, r_ls2wb.data.inst)
  r_ls2wb.data.rd            := Mux(valid_enable, ex2ls_data.data.rd, r_ls2wb.data.rd)
  r_ls2wb.data.lsu_out       := Mux(valid_enable, lsu_out, r_ls2wb.data.lsu_out)
  r_ls2wb.data.cause         := Mux(valid_enable, ex2ls_data.data.cause, r_ls2wb.data.cause)
  r_ls2wb.control.reg_w_en   := Mux(valid_enable, ex2ls_data.control.reg_w_en, r_ls2wb.control.reg_w_en)
  r_ls2wb.control.ebreak_op  := Mux(valid_enable, ex2ls_data.control.ebreak_op, r_ls2wb.control.ebreak_op)
  r_ls2wb.control.invalid_op := Mux(valid_enable, ex2ls_data.control.invalid_op, r_ls2wb.control.invalid_op)

  r_ls2wb.data.csr_w_addr  := Mux(valid_enable, ex2ls_data.data.csr_w_addr, r_ls2wb.data.csr_w_addr)
  r_ls2wb.data.csr_w_data  := Mux(valid_enable, ex2ls_data.data.csr_w_data, r_ls2wb.data.csr_w_data)
  r_ls2wb.control.csr_w_en := Mux(valid_enable, ex2ls_data.control.csr_w_en, r_ls2wb.control.csr_w_en)

  /* ========== Combinational Circuit ========== */
  io.ex2ls.ready := ready_next
  io.ls2wb.valid := r_valid

  io.ls2wb.bits := r_ls2wb

  ex2ls_data := Mux(valid_current, io.ex2ls.bits, EX2LSBundle.ex2ls_rst_val)

  ls_handler.io.addr    := ex2ls_data.data.exu_out
  ls_handler.io.w_data  := ex2ls_data.data.src2
  ls_handler.io.mem_ctl := ex2ls_data.control.mem_ctl

  io.out.state_info.rd         := Mux(ex2ls_data.control.reg_w_en, ex2ls_data.data.rd, 0.U(5.W))
  io.out.state_info.reg_w_data := lsu_out
  io.out.state_info.csr_w_addr := Mux(ex2ls_data.control.csr_w_en, ex2ls_data.data.csr_w_addr, 0.U(12.W))
  io.out.state_info.csr_w_data := ex2ls_data.data.csr_w_data
  io.out.state_info.mem_r_op   := ex2ls_data.control.mem_ctl(3)
}

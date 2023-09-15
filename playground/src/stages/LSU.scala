package stages

import chisel3._
import bundles._
import bundles.loadstore._
import utils.loadstore._

class LSUIO extends Bundle {
  val in = Input(new Bundle {
    val data    = new EX2LSDataBundle
    val control = new EX2LSControlBundle
  })
  val out = Output(new Bundle {
    val data       = new LS2WBDataBundle
    val control    = new LS2WBControlBundle
    val hazard     = new LSHazardDataBundle
    val csr_hazard = new LSCSRHazardDataBundle
  })
}

class LSU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new LSUIO)

  /* ========== Module ========== */
  val data_mem = Module(new DataMem)

  data_mem.io.addr    := io.in.data.exe_out
  data_mem.io.w_data  := io.in.data.src2
  data_mem.io.mem_ctl := io.in.control.mem_ctl

  /* ========== Combinational Circuit ========== */
  io.out.data.snpc          := io.in.data.snpc
  io.out.data.pc            := io.in.data.pc
  io.out.data.dnpc          := io.in.data.dnpc
  io.out.data.inst          := io.in.data.inst
  io.out.data.rd            := io.in.data.rd
  io.out.data.mem_out       := data_mem.io.r_data
  io.out.data.exe_out       := io.in.data.exe_out
  io.out.control.wb_ctl     := io.in.control.wb_ctl
  io.out.control.reg_w_en   := io.in.control.reg_w_en
  io.out.control.ebreak_op  := io.in.control.ebreak_op
  io.out.control.invalid_op := io.in.control.invalid_op

  io.out.data.csr_w_addr  := io.in.data.csr_w_addr
  io.out.data.csr_w_data  := io.in.data.csr_w_data
  io.out.control.csr_w_en := io.in.control.csr_w_en

  io.out.hazard.rd                 := io.in.data.rd
  io.out.hazard.rd_tag             := io.in.control.reg_w_en
  io.out.hazard.wb_ctl             := io.in.control.wb_ctl
  io.out.csr_hazard.csr_w_addr     := io.in.data.csr_w_addr
  io.out.csr_hazard.csr_w_addr_tag := io.in.control.csr_w_en
}

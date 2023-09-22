package stages

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import macros._
import bundles._
import bundles.loadstore._
import utils.loadstore._

class LSUIO extends Bundle {
  val ex2ls = Flipped(Decoupled(new EX2LSBundle))
  val ls2wb = Decoupled(new LS2WBBundle)
  val out = Output(new Bundle {
    val hazard     = new LSHazardDataBundle
    val csr_hazard = new LSCSRHazardDataBundle
  })
}

class LSU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new LSUIO)

  /* ========== Module ========== */
  val data_mem = Module(new DataMem)

  /* ========== Parameter ========== */
  val ex2ls_rst_val = (new EX2LSBundle).Lit(
    _.data -> (new EX2LSDataBundle).Lit(
      _.pc         -> CommonMacros.PC_RESET_VAL,
      _.snpc       -> CommonMacros.PC_RESET_VAL,
      _.dnpc       -> CommonMacros.PC_RESET_VAL,
      _.inst       -> CommonMacros.INST_RESET_VAL,
      _.rd         -> 0.U,
      _.src2       -> 0.U,
      _.exe_out    -> 0.U,
      _.csr_w_data -> 0.U,
      _.csr_w_addr -> 0.U
    ),
    _.control -> (new EX2LSControlBundle).Lit(
      _.mem_ctl    -> ControlMacros.MEM_CTL_DEFAULT,
      _.wb_ctl     -> ControlMacros.WB_CTL_DEFAULT,
      _.reg_w_en   -> ControlMacros.REG_W_DISABLE,
      _.invalid_op -> ControlMacros.INVALID_OP_NO,
      _.ebreak_op  -> ControlMacros.EBREAK_OP_NO,
      _.csr_w_en   -> false.B
    )
  )
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

  /* ========== Register ========== */
  val r_valid = RegInit(false.B)
  val r_ls2wb = RegInit(ls2wb_rst_val)

  /* ========== Wire ========== */
  val ready_next   = io.ex2ls.valid && !io.ex2ls.ready && (!io.ls2wb.valid || io.ls2wb.ready)
  val valid_enable = io.ex2ls.valid && !io.ex2ls.ready && (!io.ls2wb.valid || io.ls2wb.ready)
  val valid_next   = r_valid && !io.ls2wb.fire
  val ex2ls_data   = Wire(new EX2LSBundle)

  /* ========== Sequential Circuit ========== */
  r_valid := Mux(valid_enable, io.ex2ls.valid, valid_next)

  r_ls2wb.data.snpc          := ex2ls_data.data.snpc
  r_ls2wb.data.pc            := ex2ls_data.data.pc
  r_ls2wb.data.dnpc          := ex2ls_data.data.dnpc
  r_ls2wb.data.inst          := ex2ls_data.data.inst
  r_ls2wb.data.rd            := ex2ls_data.data.rd
  r_ls2wb.data.mem_out       := data_mem.io.r_data
  r_ls2wb.data.exe_out       := ex2ls_data.data.exe_out
  r_ls2wb.control.wb_ctl     := ex2ls_data.control.wb_ctl
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

  ex2ls_data := Mux(io.ex2ls.valid, io.ex2ls.bits, ex2ls_rst_val)

  data_mem.io.addr    := ex2ls_data.data.exe_out
  data_mem.io.w_data  := ex2ls_data.data.src2
  data_mem.io.mem_ctl := ex2ls_data.control.mem_ctl

  io.out.hazard.rd                 := ex2ls_data.data.rd
  io.out.hazard.rd_tag             := ex2ls_data.control.reg_w_en
  io.out.hazard.wb_ctl             := ex2ls_data.control.wb_ctl
  io.out.csr_hazard.csr_w_addr     := ex2ls_data.data.csr_w_addr
  io.out.csr_hazard.csr_w_addr_tag := ex2ls_data.control.csr_w_en
}

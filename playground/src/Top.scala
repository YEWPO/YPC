import chisel3._
import chisel3.experimental.BundleLiterals._
import stages._
import bundles._
import utils._
import hazard._
import bundles.instdecode._
import bundles.writeback._
import bundles.execute._

class Top extends Module {
  /* ========== Module ========== */
  val ifu        = Module(new IFU)
  val idu        = Module(new IDU)
  val exu        = Module(new EXU)
  val lsu        = Module(new LSU)
  val wbu        = Module(new WBU)
  val hazard     = Module(new HazardUnit)
  val csr_hazard = Module(new CSRHazardUnit)

  /* ========== Register ========== */
  val r_pre_if = Module(new PreIFReg)
  val r_if2id  = Module(new IF2IDReg)
  val r_id2ex  = Module(new ID2EXReg)
  val r_ex2ls  = Module(new EX2LSReg)
  val r_ls2wb  = Module(new LS2WBReg)

  /* ========== Sequential Circuit ========== */
  r_pre_if.io.in.data.pc := Mux(
    csr_hazard.io.expt_op,
    csr_hazard.io.expt_pc,
    Mux(exu.io.out.jump_ctl, exu.io.out.dnpc, ifu.io.out.data.snpc)
  )
  r_pre_if.io.control.enable := hazard.io.pre_if_control.enable
  r_pre_if.io.control.reset  := hazard.io.pre_if_control.reset
  r_if2id.io.in.data         := ifu.io.out.data
  r_if2id.io.control.enable  := hazard.io.if_id_control.enable
  r_if2id.io.control.reset   := hazard.io.if_id_control.reset || csr_hazard.io.csr_reset
  r_id2ex.io.in.data         := idu.io.out.data
  r_id2ex.io.in.control      := idu.io.out.control
  r_id2ex.io.control.enable  := hazard.io.id_ex_control.enable
  r_id2ex.io.control.reset   := hazard.io.id_ex_control.reset
  r_ex2ls.io.in.data         := exu.io.out.data
  r_ex2ls.io.in.control      := exu.io.out.control
  r_ex2ls.io.control.enable  := hazard.io.ex_ls_control.enable
  r_ex2ls.io.control.reset   := hazard.io.ex_ls_control.reset
  r_ls2wb.io.in.data         := lsu.io.out.data
  r_ls2wb.io.in.control      := lsu.io.out.control
  r_ls2wb.io.control.enable  := hazard.io.ls_wb_control.enable
  r_ls2wb.io.control.reset   := hazard.io.ls_wb_control.reset

  /* ========== Combinational Circuit ========== */
  ifu.io.in.data                       := r_pre_if.io.out.data
  idu.io.in.data                       := r_if2id.io.out.data
  idu.io.in.wb_data                    := wbu.io.out.data
  idu.io.in.forward.exe_E              := exu.io.out.data.exe_out
  idu.io.in.forward.snpc_E             := exu.io.out.data.snpc
  idu.io.in.forward.exe_M              := lsu.io.out.data.exe_out
  idu.io.in.forward.mem_M              := lsu.io.out.data.mem_out
  idu.io.in.forward.snpc_M             := lsu.io.out.data.snpc
  idu.io.in.forward.wb_data            := wbu.io.out.data.wb_data
  idu.io.in.csr_forward.csr_data_E     := exu.io.out.data.csr_w_data
  idu.io.in.csr_forward.csr_data_M     := lsu.io.out.data.csr_w_data
  idu.io.in.csr_forward.csr_data_W     := wbu.io.out.data.csr_w_data
  idu.io.in.hazard.fa_ctl              := hazard.io.inst_decode.control.fa_ctl
  idu.io.in.hazard.fb_ctl              := hazard.io.inst_decode.control.fb_ctl
  idu.io.in.csr_hazard.csr_forward_ctl := csr_hazard.io.inst_decode.control.csr_forward_ctl
  exu.io.in.data                       := r_id2ex.io.out.data
  exu.io.in.control                    := r_id2ex.io.out.control
  lsu.io.in.data                       := r_ex2ls.io.out.data
  lsu.io.in.control                    := r_ex2ls.io.out.control
  wbu.io.in.data                       := r_ls2wb.io.out.data
  wbu.io.in.control                    := r_ls2wb.io.out.control

  hazard.io.inst_decode.data := idu.io.out.hazard
  hazard.io.execute.data     := exu.io.out.hazard
  hazard.io.load_store.data  := lsu.io.out.hazard
  hazard.io.write_back.data  := wbu.io.out.hazard

  csr_hazard.io.inst_decode.data := idu.io.out.csr_hazard
  csr_hazard.io.execute.data     := exu.io.out.csr_hazard
  csr_hazard.io.load_store.data  := lsu.io.out.csr_hazard
  csr_hazard.io.write_back.data  := wbu.io.out.csr_hazard
}

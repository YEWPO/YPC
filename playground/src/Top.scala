import chisel3._
import stages._
import bundles._
import utils._
import hazard._

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
  r_pre_if.io.in := Mux(
    csr_hazard.io.expt_op,
    csr_hazard.io.expt_pc,
    Mux(exu.io.out.jump_ctl, exu.io.out.data.dnpc, ifu.io.out.data.snpc)
  )
}

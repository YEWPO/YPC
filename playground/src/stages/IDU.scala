package stages

import chisel3._
import bundles._
import control._
import utils.instdecode._
import utils.instdecode._

class IDUIO extends Bundle {
  val in = Input(new Bundle {
    val data    = new IF2IDDataBundle
    val control = new IF2IDControlBundle
  })
  val out = Output(new Bundle {
    val data    = new ID2EXDataBundle
    val control = new ID2EXControlBundle
  })
}

class IDU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new IDUIO)

  /* ========== Module ========== */
  val control_unit = Module(new ControlUnit)
  val imm_gen      = Module(new ImmGen)
  val gpr          = Module(new GPR)
  val gpr_forward  = Module(new GPRForward)
  val csr          = Module(new CSR)
  val csr_forward  = Module(new CSRForward)
  val csr_control  = Module(new CSRControlUnit)

}

package hazard

import chisel3._
import chisel3.util._
import macros._
import bundles.instdecode._
import bundles.execute._
import bundles.loadstore._
import bundles.writeback._

class CSRHazardUnitIO extends Bundle {
  val inst_decode = new Bundle {
    val data    = Input(new IDCSRHazardDataBundle)
    val control = Output(new IDCSRHazardControlBundle)
  }
  val execute = new Bundle {
    val data    = Input(new EXCSRHazardDataBundle)
    val control = Output(new EXCSRHazardControlBundle)
  }
  val load_store = new Bundle {
    val data    = Input(new LSCSRHazardDataBundle)
    val control = Output(new LSCSRHazardControlBundle)
  }
  val write_back = new Bundle {
    val data    = Input(new WBCSRHazardDataBundle)
    val control = Output(new WBCSRHazardControlBundle)
  }
  val expt_op   = Output(Bool())
  val expt_pc   = Output(UInt(64.W))
  val csr_reset = Bool()
}

class CSRHazardUnit extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new CSRHazardUnitIO)

  /* ========== Combinational Circuit ========== */
  val csr_fw_rules = Seq(
    (io.execute.data.csr_w_addr_tag && io.execute.data.csr_w_addr === io.inst_decode.data.csr_r_addr)       -> CSRHazardMacros.CSR_F_CTL_EXE,
    (io.load_store.data.csr_w_addr_tag && io.load_store.data.csr_w_addr === io.inst_decode.data.csr_r_addr) -> CSRHazardMacros.CSR_F_CTL_LS,
    (io.write_back.data.csr_w_addr_tag && io.write_back.data.csr_w_addr === io.inst_decode.data.csr_r_addr) -> CSRHazardMacros.CSR_F_CTL_WB,
    true.B                                                                                                  -> CSRHazardMacros.CSR_F_CTL_DEFAULT
  )

  io.inst_decode.control.csr_forward_ctl := Mux(
    io.inst_decode.data.csr_r_addr_tag,
    PriorityMux(csr_fw_rules),
    CSRHazardMacros.CSR_F_CTL_DEFAULT
  )

  io.csr_reset := io.inst_decode.data.ecall_op || io.inst_decode.data.mret_op

  io.expt_pc := Mux(io.inst_decode.data.mret_op, io.inst_decode.data.epc, io.inst_decode.data.tvec)
  io.expt_op := io.inst_decode.data.mret_op || io.inst_decode.data.ecall_op
}

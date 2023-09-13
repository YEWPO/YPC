package hazard

import chisel3._
import entity._
import chisel3.util._

object CSRHazardMacro {
  val CSR_F_CTL_DEFAULT = "b00".U
  val CSR_F_CTL_EXE     = "b01".U
  val CSR_F_CTL_LS      = "b10".U
  val CSR_F_CTL_WB      = "b11".U
}

class CSRHazardUnit extends Module {
  val inst_decode_csr_hazard = IO(Flipped(new InstDecodeCSRHazard))
  val execute_csr_hazard     = IO(Flipped(new ExecuteCSRHazard))
  val load_store_csr_hazard  = IO(Flipped(new LoadStoreCSRHazard))
  val write_back_csr_hazard  = IO(Flipped(new WriteBackCSRHazard))
  val expt_op                = IO(Output(Bool()))
  val expt_pc                = IO(Output(UInt(64.W)))

  val csr_fw_rules = Seq(
    (execute_csr_hazard.csr_w_addr_tag && execute_csr_hazard.csr_w_addr === inst_decode_csr_hazard.csr_r_addr)       -> CSRHazardMacro.CSR_F_CTL_EXE,
    (load_store_csr_hazard.csr_w_addr_tag && load_store_csr_hazard.csr_w_addr === inst_decode_csr_hazard.csr_r_addr) -> CSRHazardMacro.CSR_F_CTL_LS,
    (write_back_csr_hazard.csr_w_addr_tag && write_back_csr_hazard.csr_w_addr === inst_decode_csr_hazard.csr_r_addr) -> CSRHazardMacro.CSR_F_CTL_WB,
    true.B                                                                                                           -> CSRHazardMacro.CSR_F_CTL_DEFAULT
  )

  inst_decode_csr_hazard.csr_forward_ctl := Mux(
    inst_decode_csr_hazard.csr_r_addr_tag,
    PriorityMux(csr_fw_rules),
    CSRHazardMacro.CSR_F_CTL_DEFAULT
  )

  inst_decode_csr_hazard.csr_reset := inst_decode_csr_hazard.ecall_op || inst_decode_csr_hazard.mret_op

  expt_pc := Mux(inst_decode_csr_hazard.mret_op, inst_decode_csr_hazard.epc, inst_decode_csr_hazard.tvec)
  expt_op := inst_decode_csr_hazard.mret_op || inst_decode_csr_hazard.ecall_op
}

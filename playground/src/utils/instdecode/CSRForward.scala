package utils.instdecode

import chisel3._
import chisel3.util._
import macros._

class CSRForwardIO extends Bundle {
  val csr_data_D = Input(UInt(64.W))
  val csr_data_E = Input(UInt(64.W))
  val csr_data_M = Input(UInt(64.W))
  val csr_data_W = Input(UInt(64.W))

  val csr_fw_ctl = Input(UInt(2.W))

  val csr_data_out = Output(UInt(64.W))
}

class CSRForward extends Module {
  val io = IO(new CSRForwardIO)

  val csr_forward_map = Seq(
    CSRHazardMacros.CSR_F_CTL_DEFAULT -> io.csr_data_D,
    CSRHazardMacros.CSR_F_CTL_EXE     -> io.csr_data_E,
    CSRHazardMacros.CSR_F_CTL_LS      -> io.csr_data_M,
    CSRHazardMacros.CSR_F_CTL_WB      -> io.csr_data_W
  )

  io.csr_data_out := MuxLookup(io.csr_fw_ctl, 0.U(64.W))(csr_forward_map)
}

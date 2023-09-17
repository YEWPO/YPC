package utils.instdecode

import chisel3._
import chisel3.util._
import macros._
import bundles.instdecode._

class CSRForwardIO extends Bundle {
  val data    = Input(UInt(64.W))
  val forward = Input(new IDCSRForwardBundle)

  val fw_ctl = Input(UInt(2.W))

  val src = Output(UInt(64.W))
}

class CSRForward extends Module {
  val io = IO(new CSRForwardIO)

  val forward_map = Seq(
    CSRHazardMacros.CSR_F_CTL_DEFAULT -> io.data,
    CSRHazardMacros.CSR_F_CTL_EXE     -> io.forward.csr_data_E,
    CSRHazardMacros.CSR_F_CTL_LS      -> io.forward.csr_data_M,
    CSRHazardMacros.CSR_F_CTL_WB      -> io.forward.csr_data_W
  )

  io.src := MuxLookup(io.fw_ctl, 0.U(64.W))(forward_map)
}

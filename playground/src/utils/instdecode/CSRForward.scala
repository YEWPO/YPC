package utils.instdecode

import chisel3._
import chisel3.util._
import bundles.instdecode._

class CSRForwardIO extends Bundle {
  val addr = Input(UInt(12.W))

  val data = Input(UInt(64.W))

  val fw_info = Input(new CSRForwardInfo)

  val src = Output(UInt(64.W))
}

class CSRForward extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new CSRForwardIO)

  /* ========== Parameter ========== */
  val fw_rule = Seq(
    (io.addr === io.fw_info.addr_E) -> io.fw_info.data_E,
    (io.addr === io.fw_info.addr_M) -> io.fw_info.data_M,
    (io.addr === io.fw_info.addr_W) -> io.fw_info.data_W,
    true.B                          -> io.data
  )

  /* ========== Combinational Circuit ========== */
  io.src := Mux(io.addr.orR, PriorityMux(fw_rule), 0.U(64.W))
}

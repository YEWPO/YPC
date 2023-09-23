package utils.instdecode

import chisel3._
import chisel3.util._
import macros._
import bundles.instdecode._

class GPRForwardIO extends Bundle {
  val rs1 = UInt(5.W)
  val rs2 = UInt(5.W)

  val data1 = Input(UInt(64.W))
  val data2 = Input(UInt(64.W))

  val fw_info = Input(new GRPForwardInfo)

  val src1 = Output(UInt(64.W))
  val src2 = Output(UInt(64.W))
}

class GPRForward extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new GPRForwardIO)

  /* ========== Function ========== */
  def fw_rules(rs: UInt) = Seq(
    (rs === io.fw_info.rd_E) -> io.fw_info.exu_out,
    (rs === io.fw_info.rd_M) -> io.fw_info.lsu_out,
    (rs === io.fw_info.rd_W) -> io.fw_info.wbu_out
  )

  /* ========== Combinational Circuit ========== */
  io.src1 := Mux(io.rs1.orR, PriorityMux(fw_rules(io.rs1)), 0.U(64.W))
  io.src2 := Mux(io.rs2.orR, PriorityMux(fw_rules(io.rs2)), 0.U(64.W))
}

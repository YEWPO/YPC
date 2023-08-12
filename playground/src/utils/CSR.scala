package utils

import chisel3._
import unit._
import chisel3.util._

object CSRAddr {
  val mstatus = "h300".U(12.W)
  val mtvec   = "h305".U(12.W)
  val mepc    = "h341".U(12.W)
  val mcause  = "h342".U(12.W)
}

class CSRIO extends Bundle {
  val csr_r_en = Input(Bool())
  val csr_w_en = Input(Bool())

  val csr_r_addr = Input(UInt(12.W))
  val csr_w_addr = Input(UInt(12.W))

  val csr_w_data = Input(UInt(64.W))

  val csr_r_data = Output(UInt(64.W))
}

class CSR extends Module {
  val io = IO(new CSRIO)

  val mstatus = RegInit(CommonMacro.MSTATUS_RESET_VAL)
  val mtvec   = RegInit(0.U(64.W))
  val mepc    = RegInit(0.U(64.W))
  val mcause  = RegInit(0.U(64.W))

  val csr_map = Seq(
    CSRAddr.mstatus -> mstatus,
    CSRAddr.mtvec   -> mtvec,
    CSRAddr.mepc    -> mepc,
    CSRAddr.mcause  -> mcause
  )

  io.csr_r_data := Mux(io.csr_r_en, MuxLookup(io.csr_r_addr, 0.U(64.W))(csr_map), 0.U(64.W))

  when(io.csr_w_en) {
    when(io.csr_w_addr === CSRAddr.mstatus) {
      mstatus := io.csr_w_data
    }.elsewhen(io.csr_w_addr === CSRAddr.mtvec) {
      mtvec := io.csr_w_data
    }.elsewhen(io.csr_w_addr === CSRAddr.mepc) {
      mepc := io.csr_w_data
    }.elsewhen(io.csr_w_addr === CSRAddr.mcause) {
      mcause := io.csr_w_data
    }
  }
}

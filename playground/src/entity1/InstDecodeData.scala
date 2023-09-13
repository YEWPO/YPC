package entity

import chisel3._

class InstDecodeData extends Bundle {
  val imm  = Output(UInt(64.W))
  val src1 = Output(UInt(64.W))
  val src2 = Output(UInt(64.W))
  val rd   = Output(UInt(5.W))
  val inst = Output(UInt(32.W))
  val pc   = Output(UInt(64.W))
  val snpc = Output(UInt(64.W))
}

class InstDecodeCSRData extends Bundle {
  val csr_data   = Output(UInt(64.W))
  val csr_w_addr = Output(UInt(12.W))
  val csr_uimm   = Output(UInt(64.W))
}

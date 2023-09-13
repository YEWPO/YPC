package utils.instdecode

import chisel3._
import chisel3.util._

class CSRInfoIO extends Bundle {
  val mstatus = Input(UInt(64.W))
  val mtvec   = Input(UInt(64.W))
  val mepc    = Input(UInt(64.W))
  val mcause  = Input(UInt(64.W))
}

class CSRInfo extends BlackBox with HasBlackBoxPath {
  val io = IO(new CSRInfoIO)

  addPath("playground/src/utils/CSRInfo.v")
}

package utils.instdecode

import chisel3._
import chisel3.util._
import macros._

class GPRForwardIO extends Bundle {
  val data1 = Input(UInt(64.W))
  val data2 = Input(UInt(64.W))

  val alu_E   = Input(UInt(64.W))
  val snpc_E  = Input(UInt(64.W))
  val alu_M   = Input(UInt(64.W))
  val mem_M   = Input(UInt(64.W))
  val snpc_M  = Input(UInt(64.W))
  val wb_data = Input(UInt(64.W))

  val fa_ctl = Input(UInt(3.W))
  val fb_ctl = Input(UInt(3.W))

  val src1 = Output(UInt(64.W))
  val src2 = Output(UInt(64.W))
}

class GPRForward extends Module {
  val io = IO(new GPRForwardIO)

  val forward_map = Seq(
    HazardMacros.F_CTL_ALU_E   -> io.alu_E,
    HazardMacros.F_CTL_SNPC_E  -> io.snpc_E,
    HazardMacros.F_CTL_ALU_M   -> io.alu_M,
    HazardMacros.F_CTL_MEM_M   -> io.mem_M,
    HazardMacros.F_CTL_SNPC_M  -> io.snpc_M,
    HazardMacros.F_CTL_WB_DATA -> io.wb_data
  )

  io.src1 := MuxLookup(io.fa_ctl, io.data1)(forward_map)
  io.src2 := MuxLookup(io.fb_ctl, io.data2)(forward_map)
}

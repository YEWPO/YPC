package utils.instdecode

import chisel3._
import chisel3.util._
import macros._

class GPRForwardIO extends Bundle {
  val src = Input(UInt(64.W))

  val alu_E   = Input(UInt(64.W))
  val snpc_E  = Input(UInt(64.W))
  val alu_M   = Input(UInt(64.W))
  val mem_M   = Input(UInt(64.W))
  val snpc_M  = Input(UInt(64.W))
  val wb_data = Input(UInt(64.W))

  val f_ctl = Input(UInt(3.W))

  val out = Output(UInt(64.W))
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

  io.out := MuxLookup(io.f_ctl, io.src)(forward_map)
}

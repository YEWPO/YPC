package utils

import chisel3._
import chisel3.util._
import hazard._

class ForwardIO extends Bundle {
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

class Forward extends Module {
  val io = IO(new ForwardIO)

  val forward_map = Seq(
    HazardMacro.F_CTL_ALU_E   -> io.alu_E,
    HazardMacro.F_CTL_SNPC_E  -> io.snpc_E,
    HazardMacro.F_CTL_ALU_M   -> io.alu_M,
    HazardMacro.F_CTL_MEM_M   -> io.mem_M,
    HazardMacro.F_CTL_SNPC_M  -> io.snpc_M,
    HazardMacro.F_CTL_WB_DATA -> io.wb_data
  )

  io.out := MuxLookup(io.f_ctl, io.src)(forward_map)
}

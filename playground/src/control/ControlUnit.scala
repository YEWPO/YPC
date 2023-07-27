package control

import chisel3._

class ControlUnitIO extends Bundle {
  val inst = Input(UInt(64.W))

  val imm_type = Output(UInt(3.W))
}

object DecodeTableInfo {
  val IMM_TYPE   = 0
  val A_CTL      = 1
  val B_CTL      = 2
  val DNPC_CTL   = 3
  val ALU_CTL    = 4
  val MEM_W_EN   = 5
  val MEM_MASK   = 6
  val WB_CTL     = 7
  val REG_W_EN   = 8
  val JUMP_OP    = 9
  val EBREAK_OP  = 10
  val INVALID_OP = 11
}

class ControlUnit extends Module {
  val io = IO(new ControlUnitIO)
}

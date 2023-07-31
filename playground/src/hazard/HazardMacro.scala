package hazard

import chisel3._

object HazardMacro {
  val F_CTL_DEFAULT   = "b000".U
  val F_CTL_ALU_OUT_E = "b001".U
  val F_CTL_SNPC_E    = "b010".U
  val F_CTL_ALU_OUT_M = "b011".U
  val F_CTL_MEM_OUT_M = "b100".U
  val F_CTL_SNPC_M    = "b101".U
  val F_CTL_WB_DATA   = "b110".U
}

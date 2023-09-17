package macros

import chisel3._

object HazardMacros {
  val F_CTL_DEFAULT = "b000".U
  val F_CTL_EXE_E   = "b001".U
  val F_CTL_SNPC_E  = "b010".U
  val F_CTL_EXE_M   = "b011".U
  val F_CTL_MEM_M   = "b100".U
  val F_CTL_SNPC_M  = "b101".U
  val F_CTL_WB_DATA = "b110".U
}

object CSRHazardMacros {
  val CSR_F_CTL_DEFAULT = "b00".U
  val CSR_F_CTL_EXE     = "b01".U
  val CSR_F_CTL_LS      = "b10".U
  val CSR_F_CTL_WB      = "b11".U
}

package macros

import chisel3._

object CSRHazardMacros {
  val CSR_F_CTL_DEFAULT = "b00".U
  val CSR_F_CTL_EXE     = "b01".U
  val CSR_F_CTL_LS      = "b10".U
  val CSR_F_CTL_WB      = "b11".U
}

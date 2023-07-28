package unit

import chisel3._

object CommonMacro {
  val PC_RESET_VAL = "h8000_0000".U(64.W)
  val INST_RESET_VAL = "h13".U(32.W) // NOP instruction
}

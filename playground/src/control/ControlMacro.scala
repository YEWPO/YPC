package control

import chisel3._

object ControlMacro {
  val IMM_TYPE_DEFAULT = "b000".U
  val IMM_TYPE_R       = "b000".U
  val IMM_TYPE_I       = "b001".U
  val IMM_TYPE_S       = "b010".U
  val IMM_TYPE_B       = "b011".U
  val IMM_TYPE_U       = "b100".U
  val IMM_TYPE_J       = "b101".U

  val A_CTL_DEFAULT = false.B
  val A_CTL_SRC1    = false.B
  val A_CTL_PC      = true.B

  val B_CTL_DEFAULT = false.B
  val B_CTL_IMM     = false.B
  val B_CTL_SRC2    = true.B

  val DNPC_CTL_DEFAULT = false.B
  val DNPC_CTL_PC      = false.B
  val DNPC_CTL_SRC2    = true.B

  val ALU_CTL_DEFAULT = "h00000".U
  val ALU_CTL_ADD     = "b00000".U
  val ALU_CTL_ADDW    = "b10000".U
  val ALU_CTL_SUB     = "b00001".U
  val ALU_CTL_SUBW    = "b10001".U
  val ALU_CTL_XOR     = "b00010".U
  val ALU_CTL_OR      = "b00011".U
  val ALU_CTL_AND     = "b00100".U
  val ALU_CTL_SLT     = "b00101".U
  val ALU_CTL_SGE     = "b00110".U
  val ALU_CTL_SLTU    = "b00111".U
  val ALU_CTL_SGEU    = "b01000".U
  val ALU_CTL_EQU     = "b01001".U
  val ALU_CTL_NEQ     = "b01010".U
  val ALU_CTL_SLL     = "b01011".U
  val ALU_CTL_SLLW    = "b11011".U
  val ALU_CTL_SRL     = "b01100".U
  val ALU_CTL_SRLW    = "b11100".U
  val ALU_CTL_SRA     = "b01101".U
  val ALU_CTL_SRAW    = "b11101".U
  val ALU_CTL_MOVA    = "b01110".U
  val ALU_CTL_MOVB    = "b01111".U

  val JUMP_OP_DEFAULT = "b00".U
  val JUMP_OP_JAL     = "b01".U
  val JUMP_OP_BRANCH  = "b10".U

  val MEM_W_ENABLE  = true.B
  val MEM_W_DISABLE = false.B

  val MEM_MASK_DEFAULT = "h0000_0000_0000_0000_0000_0000_0000_0000".U
  val MEM_MASK_1BYTE   = "h0000_0000_0000_0000_0000_0000_0000_ffff".U
  val MEM_MASK_2BYTE   = "h0000_0000_0000_0000_0000_0000_ffff_ffff".U
  val MEM_MASK_4BYTE   = "h0000_0000_0000_0000_ffff_ffff_ffff_ffff".U
  val MEM_MASK_8BYTE   = "hffff_ffff_ffff_ffff_ffff_ffff_ffff_ffff".U

  val REG_W_ENABLE  = true.B
  val REG_W_DISABLE = false.B

  val WB_CTL_DEFAULT = "b00".U
  val WB_CTL_ALU     = "b00".U
  val WB_CTL_MEM     = "b01".U
  val WB_CTL_SNPC    = "b10".U

  val EBREAK_OP_YES = true.B
  val EBREAK_OP_NO  = false.B

  val INVALID_OP_YES = true.B
  val INVALID_OP_NO  = false.B
}

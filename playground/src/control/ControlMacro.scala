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

  val RS1_VALID   = true.B
  val RS1_INVALID = false.B

  val RS2_VALID   = true.B
  val RS2_INVALID = false.B

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

  val MUL_CTL_DEFAULT = "b0000".U
  val MUL_CTL_MUL     = "b0000".U
  val MUL_CTL_MULW    = "b1000".U
  val MUL_CTL_MULH    = "b0001".U
  val MUL_CTL_MULHSU  = "b0010".U
  val MUL_CTL_MULHU   = "b0011".U
  val MUL_CTL_DIV     = "b0100".U
  val MUL_CTL_DIVW    = "b1100".U
  val MUL_CTL_DIVU    = "b0101".U
  val MUL_CTL_DIVUW   = "b1101".U
  val MUL_CTL_REM     = "b0110".U
  val MUL_CTL_REMW    = "b1110".U
  val MUL_CTL_REMU    = "b0111".U
  val MUL_CTL_REMUW   = "b1111".U

  val EXE_OUT_DEFAULT = false.B
  val EXE_OUT_ALU     = false.B
  val EXE_OUT_MUL     = true.B

  val JUMP_OP_DEFAULT = "b00".U
  val JUMP_OP_JAL     = "b01".U
  val JUMP_OP_BRANCH  = "b10".U

  val MEM_CTL_DEFAULT = "b00000".U
  val MEM_CTL_LB      = "b01000".U
  val MEM_CTL_LH      = "b01001".U
  val MEM_CTL_LW      = "b01010".U
  val MEM_CTL_LD      = "b01011".U
  val MEM_CTL_LBU     = "b01100".U
  val MEM_CTL_LHU     = "b01101".U
  val MEM_CTL_LWU     = "b01110".U
  val MEM_CTL_SB      = "b10000".U
  val MEM_CTL_SH      = "b10001".U
  val MEM_CTL_SW      = "b10010".U
  val MEM_CTL_SD      = "b10011".U

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

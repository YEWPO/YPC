package control

object DecodeTable {
  val default_decode = List(
    ControlMacro.IMM_TYPE_DEFAULT,
    ControlMacro.A_CTL_DEFAULT,
    ControlMacro.B_CTL_DEFAULT,
    ControlMacro.DNPC_CTL_DEFAULT,
    ControlMacro.ALU_CTL_DEFAULT,
    ControlMacro.MEM_W_DISABLE,
    ControlMacro.MEM_MASK_DEFAULT,
    ControlMacro.WB_CTL_DEFAULT,
    ControlMacro.REG_W_DISABLE,
    ControlMacro.JUMP_OP_DEFAULT,
    ControlMacro.EBREAK_OP_NO,
    ControlMacro.INVALID_OP_YES
  )

  val decode_map = Array(
    InstList.lui -> List(
      ControlMacro.IMM_TYPE_U,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_PC,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MEM_W_DISABLE,
      ControlMacro.MEM_MASK_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    )
  )
}

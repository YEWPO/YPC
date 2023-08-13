package control

object DecodeTable {
  val default_decode = List(
    ControlMacro.IMM_TYPE_DEFAULT,
    ControlMacro.RS1_INVALID,
    ControlMacro.RS2_INVALID,
    ControlMacro.A_CTL_DEFAULT,
    ControlMacro.B_CTL_DEFAULT,
    ControlMacro.DNPC_CTL_DEFAULT,
    ControlMacro.ALU_CTL_DEFAULT,
    ControlMacro.MUL_CTL_DEFAULT,
    ControlMacro.EXE_OUT_DEFAULT,
    ControlMacro.MEM_CTL_DEFAULT,
    ControlMacro.WB_CTL_DEFAULT,
    ControlMacro.REG_W_DISABLE,
    ControlMacro.CSR_OP_NO,
    ControlMacro.JUMP_OP_DEFAULT,
    ControlMacro.EBREAK_OP_NO,
    ControlMacro.INVALID_OP_YES
  )

  val decode_map = Array(
    InstList.lui -> List(
      ControlMacro.IMM_TYPE_U,
      ControlMacro.RS1_INVALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_MOVB,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.auipc -> List(
      ControlMacro.IMM_TYPE_U,
      ControlMacro.RS1_INVALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_PC,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.addi -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.slli -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SLL,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.slti -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SLT,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.sltiu -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SLTU,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.xori -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_XOR,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.srli -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SRL,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.srai -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SRA,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.ori -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_OR,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.andi -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_AND,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.add -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.sub -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SUB,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.sll -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SLL,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.slt -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SLT,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.sltu -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SLTU,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.xor -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_XOR,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.srl -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SRL,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.sra -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SRA,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.or -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_OR,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.and -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_AND,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.addiw -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADDW,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.slliw -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SLLW,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.srliw -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SRLW,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.sraiw -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SRAW,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.addw -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADDW,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.subw -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SUBW,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.sllw -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SLLW,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.srlw -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SRLW,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.sraw -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_SRAW,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.jal -> List(
      ControlMacro.IMM_TYPE_J,
      ControlMacro.RS1_INVALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_PC,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_DEFAULT,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_SNPC,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_JAL,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.jalr -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_SRC,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_DEFAULT,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_SNPC,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_JAL,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.beq -> List(
      ControlMacro.IMM_TYPE_B,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_PC,
      ControlMacro.ALU_CTL_EQU,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_DEFAULT,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_DEFAULT,
      ControlMacro.REG_W_DISABLE,
      ControlMacro.JUMP_OP_BRANCH,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.bne -> List(
      ControlMacro.IMM_TYPE_B,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_PC,
      ControlMacro.ALU_CTL_NEQ,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_DEFAULT,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_DEFAULT,
      ControlMacro.REG_W_DISABLE,
      ControlMacro.JUMP_OP_BRANCH,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.blt -> List(
      ControlMacro.IMM_TYPE_B,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_PC,
      ControlMacro.ALU_CTL_SLT,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_DEFAULT,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_DEFAULT,
      ControlMacro.REG_W_DISABLE,
      ControlMacro.JUMP_OP_BRANCH,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.bge -> List(
      ControlMacro.IMM_TYPE_B,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_PC,
      ControlMacro.ALU_CTL_SGE,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_DEFAULT,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_DEFAULT,
      ControlMacro.REG_W_DISABLE,
      ControlMacro.JUMP_OP_BRANCH,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.bltu -> List(
      ControlMacro.IMM_TYPE_B,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_PC,
      ControlMacro.ALU_CTL_SLTU,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_DEFAULT,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_DEFAULT,
      ControlMacro.REG_W_DISABLE,
      ControlMacro.JUMP_OP_BRANCH,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.bgeu -> List(
      ControlMacro.IMM_TYPE_B,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_SRC2,
      ControlMacro.DNPC_CTL_PC,
      ControlMacro.ALU_CTL_SGEU,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_DEFAULT,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_DEFAULT,
      ControlMacro.REG_W_DISABLE,
      ControlMacro.JUMP_OP_BRANCH,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.lb -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_LB,
      ControlMacro.WB_CTL_MEM,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.lh -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_LH,
      ControlMacro.WB_CTL_MEM,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.lw -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_LW,
      ControlMacro.WB_CTL_MEM,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.ld -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_LD,
      ControlMacro.WB_CTL_MEM,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.lbu -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_LBU,
      ControlMacro.WB_CTL_MEM,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.lhu -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_LHU,
      ControlMacro.WB_CTL_MEM,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.lwu -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_LWU,
      ControlMacro.WB_CTL_MEM,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.sb -> List(
      ControlMacro.IMM_TYPE_S,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_SB,
      ControlMacro.WB_CTL_DEFAULT,
      ControlMacro.REG_W_DISABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.sh -> List(
      ControlMacro.IMM_TYPE_S,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_SH,
      ControlMacro.WB_CTL_DEFAULT,
      ControlMacro.REG_W_DISABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.sw -> List(
      ControlMacro.IMM_TYPE_S,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_SW,
      ControlMacro.WB_CTL_DEFAULT,
      ControlMacro.REG_W_DISABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.sd -> List(
      ControlMacro.IMM_TYPE_S,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_SRC1,
      ControlMacro.B_CTL_IMM,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_ADD,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_SD,
      ControlMacro.WB_CTL_DEFAULT,
      ControlMacro.REG_W_DISABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.mul -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_MUL,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.mulh -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_MULH,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.mulhsu -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_MULHSU,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.mulhu -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_MULHU,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.mulw -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_MULW,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.div -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_DIV,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.divu -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_DIVU,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.rem -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_REM,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.remu -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_REMU,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.divw -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_DIVW,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.divuw -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_DIVUW,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.remw -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_REMW,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.remuw -> List(
      ControlMacro.IMM_TYPE_R,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_VALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_REMUW,
      ControlMacro.EXE_OUT_MUL,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.csrrw -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_MOVA,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_YES,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.csrrs -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_MOVA,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_YES,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.csrrc -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_VALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_MOVA,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_YES,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.csrrwi -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_INVALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_MOVA,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_YES,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.csrrsi -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_INVALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_MOVA,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_YES,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.csrrci -> List(
      ControlMacro.IMM_TYPE_I,
      ControlMacro.RS1_INVALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_MOVA,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_ALU,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_ALU,
      ControlMacro.REG_W_ENABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_YES,
      ControlMacro.EBREAK_OP_NO,
      ControlMacro.INVALID_OP_NO
    ),
    InstList.ebreak -> List(
      ControlMacro.IMM_TYPE_DEFAULT,
      ControlMacro.RS1_INVALID,
      ControlMacro.RS2_INVALID,
      ControlMacro.A_CTL_DEFAULT,
      ControlMacro.B_CTL_DEFAULT,
      ControlMacro.DNPC_CTL_DEFAULT,
      ControlMacro.ALU_CTL_DEFAULT,
      ControlMacro.MUL_CTL_DEFAULT,
      ControlMacro.EXE_OUT_DEFAULT,
      ControlMacro.MEM_CTL_DEFAULT,
      ControlMacro.WB_CTL_DEFAULT,
      ControlMacro.REG_W_DISABLE,
      ControlMacro.JUMP_OP_DEFAULT,
      ControlMacro.CSR_OP_NO,
      ControlMacro.EBREAK_OP_YES,
      ControlMacro.INVALID_OP_NO
    )
  )
}

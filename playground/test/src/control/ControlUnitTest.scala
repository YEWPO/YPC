package control

import chisel3._
import chiseltest._
import utest._
import macros._

object ControlUnitTest extends ChiselUtestTester {
  val tests = Tests {
    test("ControlUnitTest") {
      test("U-type_lui") {
        testCircuit(new ControlUnit) { dut =>
          dut.io.inst.poke("hb7".U)
          dut.clock.step()

          dut.io.imm_type.expect(ControlMacros.IMM_TYPE_U)
          dut.io.a_ctl.expect(ControlMacros.A_CTL_DEFAULT)
          dut.io.b_ctl.expect(ControlMacros.B_CTL_IMM)
          dut.io.dnpc_ctl.expect(ControlMacros.DNPC_CTL_DEFAULT)
          dut.io.alu_ctl.expect(ControlMacros.ALU_CTL_MOVB)
          dut.io.mem_ctl.expect(ControlMacros.MEM_CTL_DEFAULT)
          dut.io.reg_w_en.expect(ControlMacros.REG_W_ENABLE)
          dut.io.jump_op.expect(ControlMacros.JUMP_OP_DEFAULT)
          dut.io.ebreak_op.expect(ControlMacros.EBREAK_OP_NO)
          dut.io.invalid_op.expect(ControlMacros.INVALID_OP_NO)
        }
      }
      test("R-type_sub") {
        testCircuit(new ControlUnit) { dut =>
          dut.io.inst.poke("h40178ab3".U)
          dut.clock.step()

          dut.io.imm_type.expect(ControlMacros.IMM_TYPE_R)
          dut.io.a_ctl.expect(ControlMacros.A_CTL_SRC1)
          dut.io.b_ctl.expect(ControlMacros.B_CTL_SRC2)
          dut.io.dnpc_ctl.expect(ControlMacros.DNPC_CTL_DEFAULT)
          dut.io.alu_ctl.expect(ControlMacros.ALU_CTL_SUB)
          dut.io.mem_ctl.expect(ControlMacros.MEM_CTL_DEFAULT)
          dut.io.reg_w_en.expect(ControlMacros.REG_W_ENABLE)
          dut.io.jump_op.expect(ControlMacros.JUMP_OP_DEFAULT)
          dut.io.ebreak_op.expect(ControlMacros.EBREAK_OP_NO)
          dut.io.invalid_op.expect(ControlMacros.INVALID_OP_NO)
        }
      }
      test("S-type_sh") {
        testCircuit(new ControlUnit) { dut =>
          dut.io.inst.poke("hf41023".U)
          dut.clock.step()

          dut.io.imm_type.expect(ControlMacros.IMM_TYPE_S)
          dut.io.a_ctl.expect(ControlMacros.A_CTL_SRC1)
          dut.io.b_ctl.expect(ControlMacros.B_CTL_IMM)
          dut.io.dnpc_ctl.expect(ControlMacros.DNPC_CTL_DEFAULT)
          dut.io.alu_ctl.expect(ControlMacros.ALU_CTL_ADD)
          dut.io.mem_ctl.expect(ControlMacros.MEM_CTL_SH)
          dut.io.reg_w_en.expect(ControlMacros.REG_W_DISABLE)
          dut.io.jump_op.expect(ControlMacros.JUMP_OP_DEFAULT)
          dut.io.ebreak_op.expect(ControlMacros.EBREAK_OP_NO)
          dut.io.invalid_op.expect(ControlMacros.INVALID_OP_NO)
        }
      }
      test("I-type_sraiw") {
        testCircuit(new ControlUnit) { dut =>
          dut.io.inst.poke("h4185551b".U)
          dut.clock.step()

          dut.io.imm_type.expect(ControlMacros.IMM_TYPE_I)
          dut.io.a_ctl.expect(ControlMacros.A_CTL_SRC1)
          dut.io.b_ctl.expect(ControlMacros.B_CTL_IMM)
          dut.io.dnpc_ctl.expect(ControlMacros.DNPC_CTL_DEFAULT)
          dut.io.alu_ctl.expect(ControlMacros.ALU_CTL_SRAW)
          dut.io.mem_ctl.expect(ControlMacros.MEM_CTL_DEFAULT)
          dut.io.reg_w_en.expect(ControlMacros.REG_W_ENABLE)
          dut.io.jump_op.expect(ControlMacros.JUMP_OP_DEFAULT)
          dut.io.ebreak_op.expect(ControlMacros.EBREAK_OP_NO)
          dut.io.invalid_op.expect(ControlMacros.INVALID_OP_NO)
        }
      }
      test("J-type_jal") {
        testCircuit(new ControlUnit) { dut =>
          dut.io.inst.poke("hf01ff0ef".U)
          dut.clock.step()

          dut.io.imm_type.expect(ControlMacros.IMM_TYPE_J)
          dut.io.a_ctl.expect(ControlMacros.A_CTL_SNPC)
          dut.io.b_ctl.expect(ControlMacros.B_CTL_DEFAULT)
          dut.io.dnpc_ctl.expect(ControlMacros.DNPC_CTL_PC)
          dut.io.alu_ctl.expect(ControlMacros.ALU_CTL_MOVA)
          dut.io.mem_ctl.expect(ControlMacros.MEM_CTL_DEFAULT)
          dut.io.reg_w_en.expect(ControlMacros.REG_W_ENABLE)
          dut.io.jump_op.expect(ControlMacros.JUMP_OP_JAL)
          dut.io.ebreak_op.expect(ControlMacros.EBREAK_OP_NO)
          dut.io.invalid_op.expect(ControlMacros.INVALID_OP_NO)
        }
      }
      test("B-type_bge") {
        testCircuit(new ControlUnit) { dut =>
          dut.io.inst.poke("he6d663".U)
          dut.clock.step()

          dut.io.imm_type.expect(ControlMacros.IMM_TYPE_B)
          dut.io.a_ctl.expect(ControlMacros.A_CTL_SRC1)
          dut.io.b_ctl.expect(ControlMacros.B_CTL_SRC2)
          dut.io.dnpc_ctl.expect(ControlMacros.DNPC_CTL_PC)
          dut.io.alu_ctl.expect(ControlMacros.ALU_CTL_SGE)
          dut.io.mem_ctl.expect(ControlMacros.MEM_CTL_DEFAULT)
          dut.io.reg_w_en.expect(ControlMacros.REG_W_DISABLE)
          dut.io.jump_op.expect(ControlMacros.JUMP_OP_BRANCH)
          dut.io.ebreak_op.expect(ControlMacros.EBREAK_OP_NO)
          dut.io.invalid_op.expect(ControlMacros.INVALID_OP_NO)
        }
      }
      test("invalid") {
        testCircuit(new ControlUnit) { dut =>
          dut.io.inst.poke("h0".U)
          dut.clock.step()

          dut.io.imm_type.expect(ControlMacros.IMM_TYPE_DEFAULT)
          dut.io.a_ctl.expect(ControlMacros.A_CTL_DEFAULT)
          dut.io.b_ctl.expect(ControlMacros.B_CTL_DEFAULT)
          dut.io.dnpc_ctl.expect(ControlMacros.DNPC_CTL_DEFAULT)
          dut.io.alu_ctl.expect(ControlMacros.ALU_CTL_DEFAULT)
          dut.io.mem_ctl.expect(ControlMacros.MEM_CTL_DEFAULT)
          dut.io.reg_w_en.expect(ControlMacros.REG_W_DISABLE)
          dut.io.jump_op.expect(ControlMacros.JUMP_OP_DEFAULT)
          dut.io.ebreak_op.expect(ControlMacros.EBREAK_OP_NO)
          dut.io.invalid_op.expect(ControlMacros.INVALID_OP_YES)
        }
      }
    }
  }
}

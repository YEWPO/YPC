package unit

import chisel3._
import chiseltest._
import utest._
import control._

object ExecuteUnitTest extends ChiselUtestTester {
  val tests = Tests {
    test("ExecuteUnit") {
      test("normal") {
        testCircuit(new ExecuteUnit) { dut =>
          dut.inst_decode_data.src1.poke("h8000_0000".U)
          dut.inst_decode_data.src2.poke(2.U)
          dut.inst_decode_data.imm.poke("b110000".U)
          dut.inst_decode_data.pc.poke("h8000_0010".U)
          dut.inst_decode_control.alu_ctl.poke(ControlMacro.ALU_CTL_SRAW)
          dut.inst_decode_control.jump_op.poke(ControlMacro.JUMP_OP_DEFAULT)
          dut.inst_decode_control.dnpc_ctl.poke(ControlMacro.DNPC_CTL_PC)
          dut.inst_decode_control.a_ctl.poke(ControlMacro.A_CTL_SRC1)
          dut.inst_decode_control.b_ctl.poke(ControlMacro.B_CTL_IMM)
          dut.clock.step()
          dut.jump_ctl.expect(false.B)
          dut.dnpc.expect("h8000_0040".U)
          dut.execute_data.exe_out.expect("hffff_ffff_ffff_8000".U)
        }
      }
      test("BranchFailed") {
        testCircuit(new ExecuteUnit) { dut =>
          dut.inst_decode_data.src1.poke(1.U)
          dut.inst_decode_data.src2.poke(1.U)
          dut.inst_decode_data.imm.poke("h10".U)
          dut.inst_decode_data.pc.poke("h8000_0200".U)
          dut.inst_decode_control.alu_ctl.poke(ControlMacro.ALU_CTL_NEQ)
          dut.inst_decode_control.jump_op.poke(ControlMacro.JUMP_OP_BRANCH)
          dut.inst_decode_control.dnpc_ctl.poke(ControlMacro.DNPC_CTL_PC)
          dut.inst_decode_control.a_ctl.poke(ControlMacro.A_CTL_SRC1)
          dut.inst_decode_control.b_ctl.poke(ControlMacro.B_CTL_SRC2)
          dut.clock.step()
          dut.jump_ctl.expect(false.B)
          dut.dnpc.expect("h8000_0210".U)
          dut.execute_data.exe_out.expect(0.U)
        }
      }
      test("BranchSuccess") {
        testCircuit(new ExecuteUnit) { dut =>
          dut.inst_decode_data.src1.poke(1.U)
          dut.inst_decode_data.src2.poke(1.U)
          dut.inst_decode_data.imm.poke("h10".U)
          dut.inst_decode_data.pc.poke("h8000_0200".U)
          dut.inst_decode_control.alu_ctl.poke(ControlMacro.ALU_CTL_SGE)
          dut.inst_decode_control.jump_op.poke(ControlMacro.JUMP_OP_BRANCH)
          dut.inst_decode_control.dnpc_ctl.poke(ControlMacro.DNPC_CTL_PC)
          dut.inst_decode_control.a_ctl.poke(ControlMacro.A_CTL_SRC1)
          dut.inst_decode_control.b_ctl.poke(ControlMacro.B_CTL_SRC2)
          dut.clock.step()
          dut.jump_ctl.expect(true.B)
          dut.dnpc.expect("h8000_0210".U)
          dut.execute_data.exe_out.expect(1.U)
        }
      }
      test("Jump") {
        testCircuit(new ExecuteUnit) { dut =>
          dut.inst_decode_data.src1.poke("h8000_0000".U)
          dut.inst_decode_data.src2.poke(1.U)
          dut.inst_decode_data.imm.poke("h10".U)
          dut.inst_decode_data.pc.poke("h8000_0200".U)
          dut.inst_decode_control.jump_op.poke(ControlMacro.JUMP_OP_JAL)
          dut.inst_decode_control.dnpc_ctl.poke(ControlMacro.DNPC_CTL_SRC2)
          dut.clock.step()
          dut.jump_ctl.expect(true.B)
          dut.dnpc.expect("h8000_0010".U)
        }
      }
    }
  }
}

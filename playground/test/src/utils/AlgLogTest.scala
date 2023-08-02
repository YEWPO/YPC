package utils

import chisel3._
import chiseltest._
import utest._
import control._

object AlgLogTest extends ChiselUtestTester {
  val tests = Tests {
    test("AlgorithmLogicUnit") {
      test("add") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke(10.U)
          dut.io.src2.poke("hffff_ffff_ffff_ffff".U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_ADD)
          dut.clock.step()
          dut.io.alu_out.expect("h0000_0000_0000_0009".U)
        }
      }
      test("addw") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("h0000_0000_ffff_fffe".U)
          dut.io.src2.poke(1.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_ADDW)
          dut.clock.step()
          dut.io.alu_out.expect("hffff_ffff_ffff_ffff".U)
        }
      }
      test("sub") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke(0.U)
          dut.io.src2.poke(1.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SUB)
          dut.clock.step()
          dut.io.alu_out.expect("hffff_ffff_ffff_ffff".U)
        }
      }
      test("subw") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke(0.U)
          dut.io.src2.poke(1.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SUBW)
          dut.clock.step()
          dut.io.alu_out.expect("hffff_ffff_ffff_ffff".U)
        }
      }
      test("xor") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("b1100".U)
          dut.io.src2.poke("b1010".U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_XOR)
          dut.clock.step()
          dut.io.alu_out.expect("b0110".U)
        }
      }
      test("or") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("b1100".U)
          dut.io.src2.poke("b1010".U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_OR)
          dut.clock.step()
          dut.io.alu_out.expect("b1110".U)
        }
      }
      test("and") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("b1100".U)
          dut.io.src2.poke("b1010".U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_AND)
          dut.clock.step()
          dut.io.alu_out.expect("b1000".U)
        }
      }
      test("slt") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("hffff_ffff_ffff_ffff".U)
          dut.io.src2.poke(0.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SLT)
          dut.clock.step()
          dut.io.alu_out.expect(1.U)
        }
      }
      test("sge") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("hffff_ffff_ffff_ffff".U)
          dut.io.src2.poke(0.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SGE)
          dut.clock.step()
          dut.io.alu_out.expect(0.U)
        }
      }
      test("sltu") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("hffff_ffff_ffff_ffff".U)
          dut.io.src2.poke(0.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SLTU)
          dut.clock.step()
          dut.io.alu_out.expect(0.U)
        }
      }
      test("sgeu") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("hffff_ffff_ffff_ffff".U)
          dut.io.src2.poke(0.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SGEU)
          dut.clock.step()
          dut.io.alu_out.expect(1.U)
        }
      }
      test("equ") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke(123456.U)
          dut.io.src2.poke(123456.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_EQU)
          dut.clock.step()
          dut.io.alu_out.expect(1.U)
        }
      }
      test("equ") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke(12345.U)
          dut.io.src2.poke(23456.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_NEQ)
          dut.clock.step()
          dut.io.alu_out.expect(1.U)
        }
      }
      test("sll") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke(1.U)
          dut.io.src2.poke(2.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SLL)
          dut.clock.step()
          dut.io.alu_out.expect("b100".U)
        }
      }
      test("sllw") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("h8000_0000".U)
          dut.io.src2.poke(2.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SLLW)
          dut.clock.step()
          dut.io.alu_out.expect(0.U)
        }
      }
      test("srl") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("h8000_0000_0000_0000".U)
          dut.io.src2.poke(2.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SRL)
          dut.clock.step()
          dut.io.alu_out.expect("h2000_0000_0000_0000".U)
        }
      }
      test("srlw") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("h0000_0001_8000_0000".U)
          dut.io.src2.poke(2.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SRLW)
          dut.clock.step()
          dut.io.alu_out.expect("h0000_0000_2000_0000".U)
        }
      }
      test("sra") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("h8000_0000_0000_0000".U)
          dut.io.src2.poke(2.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SRA)
          dut.clock.step()
          dut.io.alu_out.expect("he000_0000_0000_0000".U)
        }
      }
      test("sraw") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("h0000_0001_8000_0000".U)
          dut.io.src2.poke(2.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SRAW)
          dut.clock.step()
          dut.io.alu_out.expect("hffff_ffff_e000_0000".U)
        }
      }
      test("mova") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke(1.U)
          dut.io.src2.poke(2.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_MOVA)
          dut.clock.step()
          dut.io.alu_out.expect(1.U)
        }
      }
      test("movb") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke(1.U)
          dut.io.src2.poke(2.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_MOVB)
          dut.clock.step()
          dut.io.alu_out.expect(2.U)
        }
      }
    }
  }
}

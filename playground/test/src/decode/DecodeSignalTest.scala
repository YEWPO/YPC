package decode

import chisel3._
import utest._
import chiseltest._

object DecodeSignalTest extends ChiselUtestTester {
  val tests = Tests {
    test("DecodeSignal") {
      test("load") {
        testCircuit(new DecodeSignal) { dut =>
          dut.io.opcode.poke("b0000011".U)
          dut.io.optype.poke("b001".U)
          dut.io.opfunct.poke("b000".U)
          dut.clock.step()
          dut.io.reg_en.expect(true.B)
          dut.io.op30_en.expect(false.B)
          dut.io.mem_en.expect(true.B)
          dut.io.funct_en.expect(false.B)
          dut.io.word_en.expect(false.B)
          dut.io.branch_en.expect(false.B)
          dut.io.mem_w_en.expect(false.B)
          dut.io.a_sel_0.expect(false.B)
          dut.io.a_sel_pc.expect(false.B)
          dut.io.b_sel_imm.expect(true.B)
        }
      }
      test("store") {
        testCircuit(new DecodeSignal) { dut =>
          dut.io.opcode.poke("b0100011".U)
          dut.io.optype.poke("b010".U)
          dut.io.opfunct.poke("b000".U)
          dut.clock.step()
          dut.io.reg_en.expect(false.B)
          dut.io.op30_en.expect(false.B)
          dut.io.mem_en.expect(true.B)
          dut.io.funct_en.expect(false.B)
          dut.io.word_en.expect(false.B)
          dut.io.branch_en.expect(false.B)
          dut.io.mem_w_en.expect(true.B)
          dut.io.a_sel_0.expect(false.B)
          dut.io.a_sel_pc.expect(false.B)
          dut.io.b_sel_imm.expect(true.B)
        }
      }
      test("addiw") {
        testCircuit(new DecodeSignal) { dut =>
          dut.io.opcode.poke("b0011011".U)
          dut.io.optype.poke("b001".U)
          dut.io.opfunct.poke("b000".U)
          dut.clock.step()
          dut.io.reg_en.expect(true.B)
          dut.io.op30_en.expect(false.B)
          dut.io.mem_en.expect(false.B)
          dut.io.funct_en.expect(true.B)
          dut.io.word_en.expect(true.B)
          dut.io.branch_en.expect(false.B)
          dut.io.mem_w_en.expect(false.B)
          dut.io.a_sel_0.expect(false.B)
          dut.io.a_sel_pc.expect(false.B)
          dut.io.b_sel_imm.expect(true.B)
        }
      }
      test("jal") {
        testCircuit(new DecodeSignal) { dut =>
          dut.io.opcode.poke("b1101111".U)
          dut.io.optype.poke("b101".U)
          dut.io.opfunct.poke("b000".U)
          dut.clock.step()
          dut.io.reg_en.expect(true.B)
          dut.io.op30_en.expect(false.B)
          dut.io.mem_en.expect(false.B)
          dut.io.funct_en.expect(false.B)
          dut.io.word_en.expect(false.B)
          dut.io.branch_en.expect(false.B)
          dut.io.mem_w_en.expect(false.B)
          dut.io.a_sel_0.expect(false.B)
          dut.io.a_sel_pc.expect(true.B)
          dut.io.b_sel_imm.expect(true.B)
        }
      }
    }
  }
}

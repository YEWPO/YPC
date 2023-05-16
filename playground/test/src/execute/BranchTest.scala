package execute

import chisel3._
import chiseltest._
import utest._

object BranchTest extends ChiselUtestTester {
  val tests = Tests {
    test("enable") {
      test("equal") {
        testCircuit(new Branch) {
          dut =>
            dut.io.src1.poke(1.U)
            dut.io.src2.poke(1.U)
            dut.io.funct.poke("b000".U)
            dut.io.branch_en.poke(true.B)
            dut.clock.step()
            dut.io.res.expect(true.B)
        }
      }
      test("inequal") {
        testCircuit(new Branch) {
          dut =>
            dut.io.src1.poke(1.U)
            dut.io.src2.poke(1.U)
            dut.io.funct.poke("b001".U)
            dut.io.branch_en.poke(true.B)
            dut.clock.step()
            dut.io.res.expect(false.B)
        }
      }
      test("slt") {
        testCircuit(new Branch) {
          dut =>
            dut.io.src1.poke(2.U)
            dut.io.src2.poke("hffffffffffffffff".U)
            dut.io.funct.poke("b100".U)
            dut.io.branch_en.poke(true.B)
            dut.clock.step()
            dut.io.res.expect(false.B)
        }
      }
      test("nslt") {
        testCircuit(new Branch) {
          dut =>
            dut.io.src1.poke(2.U)
            dut.io.src2.poke("hffffffffffffffff".U)
            dut.io.funct.poke("b101".U)
            dut.io.branch_en.poke(true.B)
            dut.clock.step()
            dut.io.res.expect(true.B)
        }
      }
      test("sltu") {
        testCircuit(new Branch) {
          dut =>
            dut.io.src1.poke(2.U)
            dut.io.src2.poke("hffffffffffffffff".U)
            dut.io.funct.poke("b110".U)
            dut.io.branch_en.poke(true.B)
            dut.clock.step()
            dut.io.res.expect(true.B)
        }
      }
      test("nsltu") {
        testCircuit(new Branch) {
          dut =>
            dut.io.src1.poke(2.U)
            dut.io.src2.poke("hffffffffffffffff".U)
            dut.io.funct.poke("b111".U)
            dut.io.branch_en.poke(true.B)
            dut.clock.step()
            dut.io.res.expect(false.B)
        }
      }
    }
    test("disable") {
      testCircuit(new Branch) {
          dut =>
            dut.io.src1.poke(2.U)
            dut.io.src2.poke("hffffffffffffffff".U)
            dut.io.funct.poke("b110".U)
            dut.io.branch_en.poke(false.B)
            dut.clock.step()
            dut.io.res.expect(false.B)
      }
    }
  }
}

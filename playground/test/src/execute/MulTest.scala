package execute

import chisel3._
import chiseltest._
import utest._

object MulTest extends ChiselUtestTester {
  val tests = Tests {
    test("mul") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke(2.U)
        dut.io.src2.poke(3.U)
        dut.io.funct.poke("b000".U)
        dut.io.word_op.poke(false.B)
        dut.clock.step()
        dut.io.res.expect(6.U)
      }
    }
    test("mulh") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke("hffff_ffff_ffff_ffff".U)
        dut.io.src2.poke(2.U)
        dut.io.funct.poke("b001".U)
        dut.io.word_op.poke(false.B)
        dut.clock.step()
        dut.io.res.expect("hffff_ffff_ffff_ffff".U)
      }
    }
    test("mulhu") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke("hffff_ffff_ffff_ffff".U)
        dut.io.src2.poke(2.U)
        dut.io.funct.poke("b010".U)
        dut.io.word_op.poke(false.B)
        dut.clock.step()
        dut.io.res.expect("h0000_0000_0000_0001".U)
      }
    }
    test("mulhsu") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke("hffff_ffff_ffff_ffff".U)
        dut.io.src2.poke(2.U)
        dut.io.funct.poke("b011".U)
        dut.io.word_op.poke(false.B)
        dut.clock.step()
        dut.io.res.expect("hffff_ffff_ffff_ffff".U)
      }
    }
    test("mulw") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke("h0000_0000_ffff_ffff".U)
        dut.io.src2.poke(2.U)
        dut.io.funct.poke("b000".U)
        dut.io.word_op.poke(true.B)
        dut.clock.step()
        dut.io.res.expect("hffff_ffff_ffff_fffe".U)
      }
    }
    test("div") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke("h0000_0000_ffff_ffff".U)
        dut.io.src2.poke(0.U)
        dut.io.funct.poke("b100".U)
        dut.io.word_op.poke(false.B)
        dut.clock.step()
        dut.io.res.expect("hffff_ffff_ffff_ffff".U)
      }
    }
    test("divu") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke("h8000_0000_0000_0000".U)
        dut.io.src2.poke("hffff_ffff_ffff_ffff".U)
        dut.io.funct.poke("b101".U)
        dut.io.word_op.poke(false.B)
        dut.clock.step()
        dut.io.res.expect(0.U)
      }
    }
    test("rem") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke("h1000_0000_0000_0001".U)
        dut.io.src2.poke(0.U)
        dut.io.funct.poke("b110".U)
        dut.io.word_op.poke(false.B)
        dut.clock.step()
        dut.io.res.expect("h1000_0000_0000_0001".U)
      }
    }
    test("remu") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke("h8000_0000_0000_0000".U)
        dut.io.src2.poke("hffff_ffff_ffff_ffff".U)
        dut.io.funct.poke("b111".U)
        dut.io.word_op.poke(false.B)
        dut.clock.step()
        dut.io.res.expect("h8000_0000_0000_0000".U)
      }
    }
    test("divw") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke("h0000_0000_8000_0000".U)
        dut.io.src2.poke("h0000_0000_ffff_ffff".U)
        dut.io.funct.poke("b100".U)
        dut.io.word_op.poke(true.B)
        dut.clock.step()
        dut.io.res.expect("hffff_ffff_8000_0000".U)
      }
    }
    test("divuw") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke("h8000_0000_1000_0000".U)
        dut.io.src2.poke("hffff_ffff_ffff_ffff".U)
        dut.io.funct.poke("b101".U)
        dut.io.word_op.poke(true.B)
        dut.clock.step()
        dut.io.res.expect(0.U)
      }
    }
    test("remw") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke("h8000_0000_1000_0001".U)
        dut.io.src2.poke(0.U)
        dut.io.funct.poke("b110".U)
        dut.io.word_op.poke(true.B)
        dut.clock.step()
        dut.io.res.expect("h0000_0000_1000_0001".U)
      }
    }
    test("remuw") {
      testCircuit(new Mul) { dut =>
        dut.io.src1.poke("h1000_0000_0100_0000".U)
        dut.io.src2.poke("hffff_ffff_ffff_ffff".U)
        dut.io.funct.poke("b111".U)
        dut.io.word_op.poke(true.B)
        dut.clock.step()
        dut.io.res.expect("h0000_0000_0100_0000".U)
      }
    }
  }
}

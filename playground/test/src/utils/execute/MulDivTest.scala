package utils.execute

import chisel3._
import chiseltest._
import utest._

object MulDivTest extends ChiselUtestTester {
  val tests = Tests {
    test("mul") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke(2.U)
        dut.io.src2.poke(3.U)
        dut.io.mul_ctl.poke("b0000".U)
        dut.clock.step()
        dut.io.mul_out.expect(6.U)
      }
    }
    test("mulh") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke("hffff_ffff_ffff_ffff".U)
        dut.io.src2.poke(2.U)
        dut.io.mul_ctl.poke("b0001".U)
        dut.clock.step()
        dut.io.mul_out.expect("hffff_ffff_ffff_ffff".U)
      }
    }
    test("mulhsu") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke("hffff_ffff_ffff_ffff".U)
        dut.io.src2.poke(2.U)
        dut.io.mul_ctl.poke("b0010".U)
        dut.clock.step()
        dut.io.mul_out.expect("hffff_ffff_ffff_ffff".U)
      }
    }
    test("mulhu") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke("hffff_ffff_ffff_ffff".U)
        dut.io.src2.poke(2.U)
        dut.io.mul_ctl.poke("b0011".U)
        dut.clock.step()
        dut.io.mul_out.expect("h0000_0000_0000_0001".U)
      }
    }
    test("mulw") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke("h0000_0000_ffff_ffff".U)
        dut.io.src2.poke(2.U)
        dut.io.mul_ctl.poke("b1000".U)
        dut.clock.step()
        dut.io.mul_out.expect("hffff_ffff_ffff_fffe".U)
      }
    }
    test("div") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke("h0000_0000_ffff_ffff".U)
        dut.io.src2.poke(0.U)
        dut.io.mul_ctl.poke("b0100".U)
        dut.clock.step()
        dut.io.mul_out.expect("hffff_ffff_ffff_ffff".U)
      }
    }
    test("divu") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke("h8000_0000_0000_0000".U)
        dut.io.src2.poke("hffff_ffff_ffff_ffff".U)
        dut.io.mul_ctl.poke("b0101".U)
        dut.clock.step()
        dut.io.mul_out.expect(0.U)
      }
    }
    test("rem") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke("h1000_0000_0000_0001".U)
        dut.io.src2.poke(0.U)
        dut.io.mul_ctl.poke("b0110".U)
        dut.clock.step()
        dut.io.mul_out.expect("h1000_0000_0000_0001".U)
      }
    }
    test("remu") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke("h8000_0000_0000_0000".U)
        dut.io.src2.poke("hffff_ffff_ffff_ffff".U)
        dut.io.mul_ctl.poke("b0111".U)
        dut.clock.step()
        dut.io.mul_out.expect("h8000_0000_0000_0000".U)
      }
    }
    test("divw") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke("h0000_0000_8000_0000".U)
        dut.io.src2.poke("h0000_0000_ffff_ffff".U)
        dut.io.mul_ctl.poke("b1100".U)
        dut.clock.step()
        dut.io.mul_out.expect("hffff_ffff_8000_0000".U)
      }
    }
    test("divuw") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke("h8000_0000_1000_0000".U)
        dut.io.src2.poke("hffff_ffff_ffff_ffff".U)
        dut.io.mul_ctl.poke("b1101".U)
        dut.clock.step()
        dut.io.mul_out.expect(0.U)
      }
    }
    test("remw") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke("h8000_0000_1000_0001".U)
        dut.io.src2.poke(0.U)
        dut.io.mul_ctl.poke("b1110".U)
        dut.clock.step()
        dut.io.mul_out.expect("h0000_0000_1000_0001".U)
      }
    }
    test("remuw") {
      testCircuit(new MulDiv) { dut =>
        dut.io.src1.poke("h1000_0000_0100_0000".U)
        dut.io.src2.poke("hffff_ffff_ffff_ffff".U)
        dut.io.mul_ctl.poke("b1111".U)
        dut.clock.step()
        dut.io.mul_out.expect("h0000_0000_0100_0000".U)
      }
    }
  }
}

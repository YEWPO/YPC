package execute

import chisel3._
import chiseltest._
import utest._

object SltTest extends ChiselUtestTester {
  val tests = Tests {
    test("sltpp") {
      testCircuit(new Slt) {
        dut =>
          dut.io.src1.poke(1.S(64.W))
          dut.io.src2.poke(2.S(64.W))
          dut.clock.step()
          dut.io.res.expect(1.U(64.W))

          dut.io.src1.poke(11.S(64.W))
          dut.io.src2.poke(2.S(64.W))
          dut.clock.step()
          dut.io.res.expect(0.U(64.W))
     }
    }
    test("sltpn") {
      testCircuit(new Slt) {
        dut =>
          dut.io.src1.poke(-11.S(64.W))
          dut.io.src2.poke(2.S(64.W))
          dut.clock.step()
          dut.io.res.expect(1.U(64.W))

          dut.io.src1.poke(2.S(64.W))
          dut.io.src2.poke(-11.S(64.W))
          dut.clock.step()
          dut.io.res.expect(0.U(64.W))
      }
    }
    test("sltnn") {
      testCircuit(new Slt) {
        dut =>
          dut.io.src1.poke(-111.S(64.W))
          dut.io.src2.poke(-11.S(64.W))
          dut.clock.step()
          dut.io.res.expect(1.U(64.W))

          dut.io.src1.poke(-11.S(64.W))
          dut.io.src2.poke(-111.S(64.W))
          dut.clock.step()
          dut.io.res.expect(0.U(64.W))
      }
    }
    test("equal") {
      testCircuit(new Slt) {
        dut =>
          dut.io.src1.poke(1.S)
          dut.io.src2.poke(1.S)
          dut.clock.step()
          dut.io.res.expect(0.U)
      }
    }
  }
}

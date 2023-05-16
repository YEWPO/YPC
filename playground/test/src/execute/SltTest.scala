package execute

import chisel3._
import chiseltest._
import utest._

object SltTest extends ChiselUtestTester {
  val tests = Tests {
    test("sltpp") {
      testCircuit(new Slt) {
        dut =>
          dut.io.src1.poke(1.U(64.W))
          dut.io.src2.poke(2.U(64.W))
          dut.clock.step()
          dut.io.res.expect(1.U(64.W))

          dut.io.src1.poke(11.U(64.W))
          dut.io.src2.poke(2.U(64.W))
          dut.clock.step()
          dut.io.res.expect(0.U(64.W))
     }
    }
    test("sltpn") {
      testCircuit(new Slt) {
        dut =>
          dut.io.src1.poke("hffffffffffffffff".U(64.W))
          dut.io.src2.poke(2.U(64.W))
          dut.clock.step()
          dut.io.res.expect(1.U(64.W))

          dut.io.src1.poke(2.U(64.W))
          dut.io.src2.poke("hffffffffffffffff".U(64.W))
          dut.clock.step()
          dut.io.res.expect(0.U(64.W))
      }
    }
    test("sltnn") {
      testCircuit(new Slt) {
        dut =>
          dut.io.src1.poke("hfffffff0ffffffff".U(64.W))
          dut.io.src2.poke("hffffffffffffffff".U(64.W))
          dut.clock.step()
          dut.io.res.expect(1.U(64.W))

          dut.io.src1.poke("hffffffffffffffff".U(64.W))
          dut.io.src2.poke("hfffffff0ffffffff".U(64.W))
          dut.clock.step()
          dut.io.res.expect(0.U(64.W))
      }
    }
    test("equal") {
      testCircuit(new Slt) {
        dut =>
          dut.io.src1.poke(1.U)
          dut.io.src2.poke(1.U)
          dut.clock.step()
          dut.io.res.expect(0.U)
      }
    }
  }
}

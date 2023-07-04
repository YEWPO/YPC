package memory

import chisel3._
import chiseltest._
import utest._

object MemOpMaskTest extends ChiselUtestTester {
  val tests = Tests {
    test("MemOpMask") {
      testCircuit(new MemOpMask) { dut =>
        dut.io.funct.poke("b100".U)
        dut.clock.step()
        dut.io.signed.expect(false.B)
        dut.io.mask.expect("h0000_0000_0000_00ff".U)
      }
    }
  }
}

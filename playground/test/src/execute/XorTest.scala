package execute

import chisel3._
import chiseltest._
import utest._

class XorTest extends ChiselUtestTester {
  val tests = Tests {
    test("xor") {
      testCircuit(new Xor) {
        dut =>
          dut.io.src1.poke("b1001".U)
          dut.io.src2.poke("b1010".U)
          dut.clock.step()
          dut.io.res.expect("b0011".U)
      }
    }
  }
}

package execute

import chisel3._
import chiseltest._
import utest._

object OrTest extends ChiselUtestTester {
  val tests = Tests {
    test("or") {
      testCircuit(new Or) {
        dut =>
          dut.io.src1.poke("b1100".U)
          dut.io.src2.poke("b1010".U)
          dut.clock.step()
          dut.io.res.expect("b1110".U)
      }
    }
  }
}

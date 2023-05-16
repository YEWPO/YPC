package execute

import chisel3._
import chiseltest._
import utest._

object AndTest extends ChiselUtestTester {
  val tests = Tests {
    test("and") {
      testCircuit(new And) {
        dut =>
          dut.io.src1.poke("b1100".U)
          dut.io.src2.poke("b1001".U)
          dut.clock.step()
          dut.io.res.expect("b1000".U)
      }
    }
  }
}

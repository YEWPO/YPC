package execute

import chisel3._
import chiseltest._
import utest._

object EqualTest extends ChiselUtestTester {
  val tests = Tests {
    test("equal") {
      testCircuit(new Equal) {
        dut =>
          dut.io.src1.poke(22.U)
          dut.io.src2.poke(22.U)
          dut.clock.step()
          dut.io.res.expect(true.B)
      }
    }
    test("inequal") {
      testCircuit(new Equal) {
        dut =>
          dut.io.src1.poke(22.U)
          dut.io.src2.poke(12.U)
          dut.clock.step()
          dut.io.res.expect(false.B)
      }
    }
  }
}

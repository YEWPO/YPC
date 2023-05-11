package execute

import chisel3._
import chiseltest._
import utest._

object AddTest extends ChiselUtestTester {
  val tests = Tests {
    test("common") {
      testCircuit(new Add) {
        dut =>
          dut.io.src1.poke(1.U)
          dut.io.src2.poke(22.U)
          dut.clock.step()
          dut.io.res.expect(23.U)
      }
    }
    test("overflow") {
      testCircuit(new Add) {
        dut =>
          dut.io.src1.poke("hffffffffffffffff".U)
          dut.io.src2.poke(1.U)
          dut.clock.step()
          dut.io.res.expect(0.U)
      }
    }
  }
}

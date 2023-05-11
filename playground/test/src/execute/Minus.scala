package execute

import chisel3._
import chiseltest._
import utest._

object MinusTest extends ChiselUtestTester {
  val tests = Tests {
    test("common") {
      testCircuit(new Minus) {
        dut =>
          dut.io.src1.poke(24.U)
          dut.io.src2.poke(22.U)
          dut.clock.step()
          dut.io.res.expect(2.U)
      }
    }
    test("overflow") {
      testCircuit(new Minus) {
        dut =>
          dut.io.src1.poke(0.U)
          dut.io.src2.poke(1.U)
          dut.clock.step()
          dut.io.res.expect("hffffffffffffffff".U)
      }
    }
  }
}

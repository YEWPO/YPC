package decode

import chisel3._
import chiseltest._
import utest._

object SubOpTest extends ChiselUtestTester {
  val tests = Tests {
    test("subop") {
      testCircuit(new SubOp) {
        dut =>
          dut.io.inst.poke("h0000503b".U)
          dut.clock.step()
          dut.io.sub_op.expect(false.B)

          dut.io.inst.poke("h4000503b".U)
          dut.clock.step()
          dut.io.sub_op.expect(true.B)
      }
    }
  }
}

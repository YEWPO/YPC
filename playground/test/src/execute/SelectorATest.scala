package execute

import chisel3._
import chiseltest._
import utest._

object SelectorATest extends ChiselUtestTester {
  val tests = Tests {
    test("SelectorA") {
      testCircuit(new SelectorA) { dut =>
        dut.io.pc_val.poke("h01".U)
        dut.io.src1.poke("hffffffffffffffff".U)
        dut.io.sel_sig_0.poke(false.B)
        dut.io.sel_sig_pc.poke(true.B)
        dut.clock.step()
        dut.io.sel_out.expect("h01".U)
      }
    }
  }
}

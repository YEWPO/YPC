package execute

import chisel3._
import chiseltest._
import utest._

object SelectorBTest extends ChiselUtestTester {
  val tests = Tests {
    test("SelectorB") {
      testCircuit(new SelectorB) { dut =>
        dut.io.imm_val.poke("h01".U)
        dut.io.src2.poke("hffffffffffffffff".U)
        dut.io.sel_sig_imm.poke(true.B)
        dut.clock.step()
        dut.io.sel_out.expect("h01".U)
      }
    }
  }
}

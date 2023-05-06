package register

import chisel3._
import chiseltest._
import utest._

object PCTest extends ChiselUtestTester {
  val tests = Tests {
    test("reset") {
      testCircuit(new PC) {
        dut =>
          dut.reset.poke(true.B)
          dut.clock.step()
          dut.io.pc.expect("h80000000".U)
      }
    }
    test("value") {
      testCircuit(new PC) {
        dut =>
          dut.reset.poke(true.B)
          dut.clock.step()
          dut.io.pc.expect("h80000000".U)
          dut.reset.poke(false.B)
          dut.io.next_pc.poke("h80200000".U)
          dut.clock.step()
          dut.io.pc.expect("h80200000".U)
      }
    }
  }
}

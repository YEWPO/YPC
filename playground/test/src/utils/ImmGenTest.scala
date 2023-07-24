package utils

import chisel3._
import chiseltest._
import utest._

object ImmGenTest extends ChiselUtestTester {
  val tests = Tests {
    test("ImmGen") {
      testCircuit(new ImmGen()) { dut =>
        dut.io.in.poke(0.U(25.W))
        dut.io.imm_type.poke(1.U(3.W))
        dut.clock.step()
        dut.io.imm_out.expect(0.U(64.W))
      }
    }
  }
}

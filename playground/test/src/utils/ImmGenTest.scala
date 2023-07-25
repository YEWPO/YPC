package utils

import chisel3._
import chiseltest._
import utest._

object ImmGenTest extends ChiselUtestTester {
  val tests = Tests {
    test("ImmGen") {
      testCircuit(new ImmGen()) { dut =>
        dut.io.in.poke("h1ff8202".U(25.W));
        dut.io.imm_type.poke(ImmType.IMM_TYPE_I)
        dut.clock.step()
        dut.io.imm_out.expect("hfffffffffffffffc".U(64.W))
      }
    }
  }
}

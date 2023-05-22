package decode

import chisel3._
import chiseltest._
import utest._

object ImmPartTest extends ChiselUtestTester {
  val tests = Tests {
    test("1") {
      testCircuit(new ImmPart) {
        dut =>
          dut.io.inst.poke("h00000297".U)
          dut.io.op_type.poke("b100".U)
          dut.clock.step()
          dut.io.imm_out.expect("h0".U)
      }
    }
    test("2") {
      testCircuit(new ImmPart) {
        dut =>
          dut.io.inst.poke("h0002b823".U)
          dut.io.op_type.poke("b010".U)
          dut.clock.step()
          dut.io.imm_out.expect("h10".U)
      }
    }
    test("3") {
      testCircuit(new ImmPart) {
        dut =>
          dut.io.inst.poke("h0102b503".U)
          dut.io.op_type.poke("b001".U)
          dut.clock.step()
          dut.io.imm_out.expect("h10".U)
      }
    }
  }
}

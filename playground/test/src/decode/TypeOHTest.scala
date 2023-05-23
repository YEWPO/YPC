package decode

import chisel3._
import chiseltest._
import utest._

object TypeOHTest extends ChiselUtestTester {
  val tests = Tests {
    test("none") {
      testCircuit(new TypeOH) {
        dut =>
          dut.io.op_type.poke("b000".U)
          dut.clock.step()
          dut.io.J_type.expect(false.B)
          dut.io.B_type.expect(false.B)
          dut.io.S_type.expect(false.B)
      }
    }
    test("J") {
      testCircuit(new TypeOH) {
        dut =>
          dut.io.op_type.poke("b101".U)
          dut.clock.step()
          dut.io.J_type.expect(true.B)
          dut.io.B_type.expect(false.B)
          dut.io.S_type.expect(false.B)
      }
    }
    test("B") {
      testCircuit(new TypeOH) {
        dut =>
          dut.io.op_type.poke("b011".U)
          dut.clock.step()
          dut.io.J_type.expect(false.B)
          dut.io.B_type.expect(true.B)
          dut.io.S_type.expect(false.B)
      }
    }
    test("S") {
      testCircuit(new TypeOH) {
        dut =>
          dut.io.op_type.poke("b010".U)
          dut.clock.step()
          dut.io.J_type.expect(false.B)
          dut.io.B_type.expect(false.B)
          dut.io.S_type.expect(true.B)
      }
    }
  }
}

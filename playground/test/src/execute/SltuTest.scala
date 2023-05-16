package execute

import chisel3._
import chiseltest._
import utest._

object SltuTest extends ChiselUtestTester {
  val tests = Tests {
    test("greater") {
      testCircuit(new Sltu) {
        dut =>
          dut.io.src1.poke(11.U)
          dut.io.src2.poke(2.U)
          dut.clock.step()
          dut.io.res.expect(0.U)
      }
    }
    test("equal") {
      testCircuit(new Sltu) {
        dut =>
          dut.io.src1.poke("hffffffffffffffff".U)
          dut.io.src2.poke("hffffffffffffffff".U)
          dut.clock.step()
          dut.io.res.expect(0.U)
      }
    }
    test("below") {
      testCircuit(new Sltu) {
        dut =>
          dut.io.src1.poke(0.U)
          dut.io.src2.poke(5.U)
          dut.clock.step()
          dut.io.res.expect(1.U)
      }
    }
  }
}

package decode

import chisel3._
import utest._
import chiseltest._

object DecodeSignalTest extends ChiselUtestTester {
  val tests = Tests {
    test("DecodeSignal") {
      testCircuit(new DecodeSignal) { dut =>
        dut.io.opcode.poke("b0000011".U)
        dut.io.optype.poke("b010".U)
        dut.clock.step()
        dut.io.reg_en.expect(false.B)
        dut.io.op30_en.expect(false.B)
        dut.io.mem_en.expect(true.B)
        dut.io.funct_en.expect(true.B)
        dut.io.word_en.expect(false.B)
      }
    }
  }
}

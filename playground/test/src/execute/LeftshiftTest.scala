package execute

import chisel3._
import chiseltest._
import utest._

object LeftshiftTest extends ChiselUtestTester {
  val tests = Tests {
    test("64") {
      testCircuit(new Leftshift) {
        dut =>
          dut.io.src1.poke("b10110100".U)
          dut.io.src2.poke(3.U)
          dut.io.word_op.poke(false.B)
          dut.clock.step()
          dut.io.res.expect("b10110100000".U)

          dut.io.src1.poke("hf000000000000000".U)
          dut.io.src2.poke(3.U)
          dut.io.word_op.poke(false.B)
          dut.clock.step()
          dut.io.res.expect("h8000000000000000".U)
      }
    }
    test("32") {
      testCircuit(new Leftshift) {
        dut =>
          dut.io.src1.poke("b10110100".U)
          dut.io.src2.poke(3.U)
          dut.io.word_op.poke(true.B)
          dut.clock.step()
          dut.io.res.expect("b10110100000".U)

          dut.io.src1.poke("hf0000000".U)
          dut.io.src2.poke(3.U)
          dut.io.word_op.poke(true.B)
          dut.clock.step()
          dut.io.res.expect("hffffffff80000000".U)
      }
    }
  }
}

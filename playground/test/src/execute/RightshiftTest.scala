package execute

import chisel3._
import chiseltest._
import utest._

object RightshiftTest extends ChiselUtestTester {
  val tests = Tests {
    test("32") {
      test("logic") {
        testCircuit(new Rightshift) {
          dut =>
            dut.io.src1.poke("hffffffff".U)
            dut.io.src2.poke("h21".U)
            dut.io.sr_op.poke(false.B)
            dut.io.word_op.poke(true.B)
            dut.clock.step()
            dut.io.res.expect("h000000007fffffff".U)
        }
      }
      test("algorithm") {
        testCircuit(new Rightshift) {
          dut =>
            dut.io.src1.poke("hffffffff".U)
            dut.io.src2.poke("h21".U)
            dut.io.sr_op.poke(true.B)
            dut.io.word_op.poke(true.B)
            dut.clock.step()
            dut.io.res.expect("hffffffffffffffff".U)
        }
      }
    }
    test("64") {
      test("logic") {
        testCircuit(new Rightshift) {
          dut =>
            dut.io.src1.poke("hffffffffffffffff".U)
            dut.io.src2.poke("h21".U)
            dut.io.sr_op.poke(false.B)
            dut.io.word_op.poke(false.B)
            dut.clock.step()
            dut.io.res.expect("h7fffffff".U)
        }
      }
      test("algorithm") {
        testCircuit(new Rightshift) {
          dut =>
            dut.io.src1.poke("hffffffffffffffff".U)
            dut.io.src2.poke("h21".U)
            dut.io.sr_op.poke(true.B)
            dut.io.word_op.poke(false.B)
            dut.clock.step()
            dut.io.res.expect("hffffffffffffffff".U)
        }
      }
    }
  }
}

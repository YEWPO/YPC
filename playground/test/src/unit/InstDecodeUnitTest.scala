package unit

import chisel3._
import chiseltest._
import utest._

object InstDecodeUnitTest extends ChiselUtestTester {
  val tests = Tests {
    test("InstDecodeUnit") {
      test("ResetOn") {
        testCircuit(new InstDecodeUnit()) { dut =>
          dut.io.pc_f.poke("h10001073".U)
          dut.io.enable.poke(true.B)
          dut.io.reset.poke(true.B)
          dut.clock.step()
          dut.io.pc_d.expect("h80000000".U)
        }
      }
      test("ResetOff") {
        testCircuit(new InstDecodeUnit()) { dut =>
          dut.io.pc_f.poke("h10001073".U)
          dut.io.enable.poke(true.B)
          dut.io.reset.poke(false.B)
          dut.clock.step()
          dut.io.pc_d.expect("h10001073".U)
        }
      }
      test("Disable") {
        testCircuit(new InstDecodeUnit()) { dut =>
          dut.io.pc_f.poke("h10001073".U)
          dut.io.enable.poke(true.B)
          dut.io.reset.poke(false.B)
          dut.clock.step()
          dut.io.pc_d.expect("h10001073".U)

          dut.io.pc_f.poke("h10001023".U)
          dut.io.enable.poke(false.B)
          dut.io.reset.poke(false.B)
          dut.clock.step()
          dut.io.pc_d.expect("h10001073".U)
        }
      }
    }
  }
}

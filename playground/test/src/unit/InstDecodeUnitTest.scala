package unit

import chisel3._
import chiseltest._
import utest._

object InstDecodeUnitTest extends ChiselUtestTester {
  val tests = Tests {
    test("InstDecodeUnit") {
      test("ResetOn") {
        testCircuit(new InstDecodeUnit) { dut =>
          dut.inst_fetch_data.pc.poke("h10001073".U)
          dut.inst_decode_hazard.enable.poke(true.B)
          dut.inst_decode_hazard.reset.poke(true.B)
          dut.clock.step()
          dut.inst_decode_data.pc.expect("h80000000".U)
        }
      }
      test("ResetOff") {
        testCircuit(new InstDecodeUnit) { dut =>
          dut.inst_fetch_data.pc.poke("h10001073".U)
          dut.inst_decode_hazard.enable.poke(true.B)
          dut.inst_decode_hazard.reset.poke(false.B)
          dut.clock.step()
          dut.inst_decode_data.pc.expect("h10001073".U)
        }
      }
      test("Disable") {
        testCircuit(new InstDecodeUnit) { dut =>
          dut.inst_fetch_data.pc.poke("h10001073".U)
          dut.inst_decode_hazard.enable.poke(true.B)
          dut.inst_decode_hazard.reset.poke(false.B)
          dut.clock.step()
          dut.inst_decode_data.pc.expect("h10001073".U)

          dut.inst_fetch_data.pc.poke("h10001023".U)
          dut.inst_decode_hazard.enable.poke(false.B)
          dut.inst_decode_hazard.reset.poke(false.B)
          dut.clock.step()
          dut.inst_decode_data.pc.expect("h10001073".U)
        }
      }
    }
  }
}

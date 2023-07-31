package unit

import chisel3._
import chiseltest._
import utest._

object InstFetchUnitTest extends ChiselUtestTester {
  val tests = Tests {
    test("InstFetchUnit") {
      test("Enable") {
        testCircuit(new InstFetchUnit()) { dut =>
          dut.npc.poke("h8000_0040".U)
          dut.inst_fetch_hazard.enable.poke(true.B)
          dut.clock.step()
          dut.inst_fetch_data.pc.expect("h8000_0040".U)
        }
      }
      test("Disable") {
        testCircuit(new InstFetchUnit()) { dut =>
          dut.npc.poke("h8000_0040".U)
          dut.inst_fetch_hazard.enable.poke(false.B)
          dut.clock.step()
          dut.inst_fetch_data.pc.expect("h8000_0000".U)
        }
      }
    }
  }
}

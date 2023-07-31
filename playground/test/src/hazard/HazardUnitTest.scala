package hazard

import chisel3._
import chiseltest._
import utest._

object HazardUnitTest extends ChiselUtestTester {
  val tests = Tests {
    test("HazardUnit") {
      test("NoHazard") {
        testCircuit(new HazardUnit) { dut =>
        }
      }
    }
  }
}

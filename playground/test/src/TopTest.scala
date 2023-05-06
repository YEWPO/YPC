import chisel3._
import chiseltest._
import utest._

object TopTest extends ChiselUtestTester {
  val tests = Tests {
    test("top") {
      testCircuit(new Top) {
        dut =>
          dut.io.inst.poke("hffc10113".U)
          dut.clock.step()
          dut.io.regOut(2).expect("hfffffffffffffffc".U)
          dut.io.pc.expect("h80000004".U)
      }
    }
  }
}

import chisel3._
import chiseltest._
import utest._

object MuxTest extends ChiselUtestTester {
  val tests = Tests {
    test("Mux") {
      testCircuit(new Mux) {
        dut =>
          for (i <- 0 to 31) {
            dut.io.regInput(i).poke(i.asUInt)
          }

          for (i <- 0 to 31) {
            dut.io.idx.poke(i.asUInt)
            dut.clock.step()
            dut.io.out.expect(i.asUInt)
          }
      }
    }
  }
}

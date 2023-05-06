import chisel3._
import chiseltest._
import utest._

object AdderTest extends ChiselUtestTester {
  val tests = Tests {
    test("Adder") {
      testCircuit(new Adder) {
        dut =>
          dut.io.a.poke(10.U)
          dut.io.b.poke(111.U)
          dut.clock.step()
          dut.io.out.expect(121.U)
      }
    }
    test("overflow") {
      testCircuit(new Adder) {
        dut =>
          dut.io.a.poke("hffffffffffffffff".U)
          dut.io.b.poke("h0000000000000002".U)
          dut.io.out.expect(1.U)
      }
    }
  }
}

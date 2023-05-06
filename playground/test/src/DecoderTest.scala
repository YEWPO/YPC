import chisel3._
import chiseltest._
import utest._

object DecoderTest extends ChiselUtestTester {
  val tests = Tests {
    test("Decoder") {
      testCircuit(new Decoder) {
        dut =>
          dut.io.inst.poke("hffc10113".U)
          dut.io.rs1.expect("h2".U)
          dut.io.rd.expect("h2".U)
          dut.io.imm.expect("hfffffffffffffffc".U)
      }
    }
  }
}

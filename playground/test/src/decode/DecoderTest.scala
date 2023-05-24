package decode

import chisel3._
import chiseltest._
import utest._

object DecoderTest extends ChiselUtestTester {
  val tests = Tests {
    test("decoder") {
      testCircuit(new Decoder) {
        dut =>
          dut.io.inst.poke("h00113423".U)
          dut.io.src1.poke("h80008ff0".U)
          dut.io.src2.poke("h10".U)
          dut.io.pc.poke("h80000024".U)

          dut.io.out_src1.expect("h80008ff0".U)
          dut.io.out_src2.expect("h8".U)
          dut.io.sub_op.expect(false.B)
          dut.io.word_op.expect(false.B)
          dut.io.S_type.expect(true.B)
      }
    }
  }
}

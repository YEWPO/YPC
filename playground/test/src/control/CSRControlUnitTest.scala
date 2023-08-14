package control

import chisel3._
import chiseltest._
import utest._

object CSRControlUnitTest extends ChiselUtestTester {
  val tests = Tests {
    test("CSRControlUnit") {
      def testCSRControl(zicsr_op: Boolean, rd: Int, rs1: Int, funct: Int): Unit = {
        val r_en = zicsr_op && rd != 0
        val w_en = zicsr_op && rs1 != 0
        val src_ctl = (funct & 4) != 0
        val op_ctl = funct & 3
        testCircuit(new CSRControlUnit) { dut =>
          dut.io.zicsr_op.poke(zicsr_op.B)
          dut.io.rd.poke(rd.U)
          dut.io.rs1.poke(rs1.U)
          dut.io.funct.poke(funct.U)
          dut.clock.step()
          dut.io.csr_r_en.expect(r_en.B)
          dut.io.csr_w_en.expect(w_en.B)
          dut.io.csr_src_ctl.expect(src_ctl.B)
          dut.io.csr_op_ctl.expect(op_ctl.U)
        }
      }

      val test_data = List[(Boolean, Int, Int, Int)](
        (true, 1, 1, 1),
        (false, 1, 1, 2),
        (true, 0, 1, 3),
        (true, 1, 0, 5)
      )
      test_data.foreach( data => testCSRControl(data._1, data._2, data._3, data._4))
    }
  }
}

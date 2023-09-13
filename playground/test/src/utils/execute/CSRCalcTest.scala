package utils.execute

import chisel3._
import chiseltest._
import utest._
import scala.util.Random

object CSRCalcTest extends ChiselUtestTester {
  val tests = Tests {
    test("CSROperation") {
      def testCSROperation(): Unit = {
        val rand = new Random
        val src1: Int = rand.nextInt(10)
        val src2: Int = rand.nextInt(10)
        val op: Int = rand.nextInt(4)
        val ans: Int = op match {
          case 0 => 0
          case 1 => src1
          case 2 => src2 | src1
          case 3 => src2 & ~src1
        }
        testCircuit(new CSROperation) { dut =>
          dut.io.src.poke(src1.U)
          dut.io.csr_data.poke(src2.U)
          dut.io.csr_op_ctl.poke(op.U)
          dut.clock.step()
          dut.io.csr_op_out.expect(ans.U)
        }
      }

      for (i <- 1 to 10) {
        testCSROperation()
      }
    }
  }
}

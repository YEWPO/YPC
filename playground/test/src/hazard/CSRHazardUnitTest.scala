package hazard

import chisel3._
import chiseltest._
import utest._
import utils._

object CSRHazardUnitTest extends ChiselUtestTester {
  val tests = Tests {
    test("CSRHazardUnit") {
      test("CSRForward") {
        def testForward(testcase: Int): Unit = {
          val exe_tag = testcase <= 1 && testcase != 0
          val mem_tag = testcase <= 2 && testcase != 0
          val wb_tag = testcase <= 3 && testcase != 0
          testCircuit(new CSRHazardUnit) { dut =>
            dut.execute_csr_hazard.csr_w_addr.poke(CSRAddr.mstatus)
            dut.execute_csr_hazard.csr_w_addr_tag.poke(exe_tag.B)
            dut.load_store_csr_hazard.csr_w_addr.poke(CSRAddr.mstatus)
            dut.load_store_csr_hazard.csr_w_addr_tag.poke(mem_tag.B)
            dut.write_back_csr_hazard.csr_w_addr.poke(CSRAddr.mstatus)
            dut.write_back_csr_hazard.csr_w_addr_tag.poke(wb_tag.B)
            dut.inst_decode_csr_hazard.csr_r_addr.poke(CSRAddr.mstatus)
            dut.inst_decode_csr_hazard.csr_r_addr_tag.poke(true.B)
            dut.clock.step()
            dut.inst_decode_csr_hazard.csr_forward_ctl.expect(testcase.U)
          }
        }

        for (i <- 0 to 3) {
          testForward(i)
        }
      }
      test("CSRReset") {
        def testReset(testcase: Boolean): Unit = {
          testCircuit(new CSRHazardUnit) { dut =>
            dut.inst_decode_csr_hazard.mret_op.poke(testcase.B)
            dut.clock.step()
            dut.inst_decode_csr_hazard.csr_reset.expect(testcase.B)
            dut.expt_op.expect(testcase.B)
          }
        }

        testReset(true)
        testReset(false)
      }
    }
  }
}

package utils

import chisel3._
import chiseltest._
import utest._
import unit._

object CSRTest extends ChiselUtestTester {
  val tests = Tests {
    test("CSR") {
      test("write mstatus") {
        testCircuit(new CSR) { dut =>
          dut.io.csr_r_addr.poke(CSRAddr.mstatus)
          dut.io.csr_r_en.poke(true.B)
          dut.io.csr_w_addr.poke(CSRAddr.mstatus)
          dut.io.csr_w_en.poke(true.B)
          dut.io.csr_w_data.poke("h1234".U)
          dut.clock.step()
          dut.io.csr_r_data.expect("h1234".U)
        }
      }
      test("write mepc") {
        testCircuit(new CSR) { dut =>
          dut.io.csr_r_addr.poke(CSRAddr.mepc)
          dut.io.csr_r_en.poke(true.B)
          dut.io.csr_w_addr.poke(CSRAddr.mepc)
          dut.io.csr_w_en.poke(true.B)
          dut.io.csr_w_data.poke("h1234".U)
          dut.clock.step()
          dut.io.csr_r_data.expect("h1234".U)
        }
      }
      test("read mstatus") {
        testCircuit(new CSR) { dut =>
          dut.io.csr_r_addr.poke(CSRAddr.mstatus)
          dut.io.csr_r_en.poke(true.B)
          dut.io.csr_w_addr.poke(CSRAddr.mepc)
          dut.io.csr_w_en.poke(true.B)
          dut.io.csr_w_data.poke("h1234".U)
          dut.clock.step()
          dut.io.csr_r_data.expect(CommonMacro.MSTATUS_RESET_VAL)
        }
      }
      test("exception") {
        testCircuit(new CSR) { dut =>
          dut.io.expt_op.poke(true.B)
          dut.io.pc.poke("h80001000".U)
          dut.io.csr_r_addr.poke(CSRAddr.mepc)
          dut.io.csr_r_en.poke(true.B)
          dut.clock.step()
          dut.io.csr_r_data.expect("h80001000".U)
        }
      }
    }
  }
}

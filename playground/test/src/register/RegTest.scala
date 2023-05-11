package register

import chisel3._
import chiseltest._
import utest._

object RegsTest extends ChiselUtestTester {
  val tests = Tests {
    test("zero") {
      testCircuit(new Regs) {
        dut =>
          dut.io.w_data.poke(12.U)
          dut.io.w_en.poke(true.B)
          dut.io.w_rd.poke(0.U)
          dut.io.r_rs1.poke(0.U)
          dut.io.r_rs2.poke(1.U)
          dut.clock.step()
          dut.io.r_data1.expect(0.U)
          dut.io.r_data2.expect(0.U)
      }
    }
    test("disable") {
      testCircuit(new Regs) {
        dut =>
          dut.io.w_data.poke(12.U)
          dut.io.w_en.poke(false.B)
          dut.io.w_rd.poke(1.U)
          dut.io.r_rs1.poke(0.U)
          dut.io.r_rs2.poke(1.U)
          dut.clock.step()
          dut.io.r_data1.expect(0.U)
          dut.io.r_data2.expect(0.U)
      }
    }
    test("others") {
      testCircuit(new Regs) {
        dut =>
          dut.io.w_data.poke(12.U)
          dut.io.w_en.poke(true.B)
          dut.io.w_rd.poke(6.U)
          dut.io.r_rs1.poke(1.U)
          dut.io.r_rs2.poke(6.U)
          dut.clock.step()
          dut.io.r_data1.expect(0.U)
          dut.io.r_data2.expect(12.U)

          dut.io.w_data.poke(12.U)
          dut.io.w_en.poke(true.B)
          dut.io.w_rd.poke(1.U)
          dut.io.r_rs1.poke(1.U)
          dut.io.r_rs2.poke(6.U)
          dut.clock.step()
          dut.io.r_data1.expect(12.U)
          dut.io.r_data2.expect(12.U)
      }
    }
  }
}

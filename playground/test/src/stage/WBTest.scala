package stage

import chisel3._
import chiseltest._
import utest._

object WBTest extends ChiselUtestTester {
  val tests = Tests {
    test("WB") {
      test("1") {
        testCircuit(new WriteBackUnit) { dut =>
          dut.io.in.data.snpc.poke(1.U)
          dut.io.in.data.mem_out.poke(2.U)
          dut.io.in.data.exe_out.poke(3.U)
          dut.io.in.control.wb_ctl.poke(0.U)
          dut.clock.step()
          dut.io.out.wb_data.expect(3.U)
        }
      }
      test("2") {
        testCircuit(new WriteBackUnit) { dut =>
          dut.io.in.data.snpc.poke(1.U)
          dut.io.in.data.mem_out.poke(2.U)
          dut.io.in.data.exe_out.poke(3.U)
          dut.io.in.control.wb_ctl.poke(1.U)
          dut.clock.step()
          dut.io.out.wb_data.expect(2.U)
        }
      }
      test("3") {
        testCircuit(new WriteBackUnit) { dut =>
          dut.io.in.data.snpc.poke(1.U)
          dut.io.in.data.mem_out.poke(2.U)
          dut.io.in.data.exe_out.poke(3.U)
          dut.io.in.control.wb_ctl.poke(2.U)
          dut.clock.step()
          dut.io.out.wb_data.expect(1.U)
        }
      }
      test("4") {
        testCircuit(new WriteBackUnit) { dut =>
          dut.io.in.data.snpc.poke(1.U)
          dut.io.in.data.mem_out.poke(2.U)
          dut.io.in.data.exe_out.poke(3.U)
          dut.io.in.control.wb_ctl.poke(3.U)
          dut.clock.step()
          dut.io.out.wb_data.expect(0.U)
        }
      }
    }
  }
}

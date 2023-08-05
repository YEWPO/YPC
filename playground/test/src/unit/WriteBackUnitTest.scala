package unit

import chisel3._
import chiseltest._
import utest._

object WriteBackUnitTest extends ChiselUtestTester {
  val tests = Tests {
    test("WriteBackUnit") {
      test("1") {
        testCircuit(new WriteBackUnit) { dut =>
          dut.load_store_data.snpc.poke(1.U)
          dut.load_store_data.mem_out.poke(2.U)
          dut.load_store_data.exe_out.poke(3.U)
          dut.load_store_control.wb_ctl.poke(0.U)
          dut.clock.step()
          dut.write_back_data.wb_data.expect(3.U)
        }
      }
      test("2") {
        testCircuit(new WriteBackUnit) { dut =>
          dut.load_store_data.snpc.poke(1.U)
          dut.load_store_data.mem_out.poke(2.U)
          dut.load_store_data.exe_out.poke(3.U)
          dut.load_store_control.wb_ctl.poke(1.U)
          dut.clock.step()
          dut.write_back_data.wb_data.expect(2.U)
        }
      }
      test("3") {
        testCircuit(new WriteBackUnit) { dut =>
          dut.load_store_data.snpc.poke(1.U)
          dut.load_store_data.mem_out.poke(2.U)
          dut.load_store_data.exe_out.poke(3.U)
          dut.load_store_control.wb_ctl.poke(2.U)
          dut.clock.step()
          dut.write_back_data.wb_data.expect(1.U)
        }
      }
      test("4") {
        testCircuit(new WriteBackUnit) { dut =>
          dut.load_store_data.snpc.poke(1.U)
          dut.load_store_data.mem_out.poke(2.U)
          dut.load_store_data.exe_out.poke(3.U)
          dut.load_store_control.wb_ctl.poke(3.U)
          dut.clock.step()
          dut.write_back_data.wb_data.expect(0.U)
        }
      }
     }
  }
}

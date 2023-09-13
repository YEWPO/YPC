package utils.instdecode

import chisel3._
import chiseltest._
import utest._
import hazard._

object GPRForwardTest extends ChiselUtestTester {
  val tests = Tests {
    test("Forward") {
      test("Default") {
        testCircuit(new Forward) { dut =>
          dut.io.src.poke(1.U)
          dut.io.alu_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.alu_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.f_ctl.poke(0.U)
          dut.clock.step()
          dut.io.out.expect(1.U)
        }
      }
      test("alu_E") {
        testCircuit(new Forward) { dut =>
          dut.io.src.poke(1.U)
          dut.io.alu_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.alu_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.f_ctl.poke(HazardMacro.F_CTL_ALU_E)
          dut.clock.step()
          dut.io.out.expect(2.U)
        }
      }
      test("snpc_E") {
        testCircuit(new Forward) { dut =>
          dut.io.src.poke(1.U)
          dut.io.alu_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.alu_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.f_ctl.poke(HazardMacro.F_CTL_SNPC_E)
          dut.clock.step()
          dut.io.out.expect(3.U)
        }
      }
      test("alu_M") {
        testCircuit(new Forward) { dut =>
          dut.io.src.poke(1.U)
          dut.io.alu_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.alu_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.f_ctl.poke(HazardMacro.F_CTL_ALU_M)
          dut.clock.step()
          dut.io.out.expect(4.U)
        }
      }
      test("mem_M") {
        testCircuit(new Forward) { dut =>
          dut.io.src.poke(1.U)
          dut.io.alu_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.alu_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.f_ctl.poke(HazardMacro.F_CTL_MEM_M)
          dut.clock.step()
          dut.io.out.expect(5.U)
        }
      }
      test("snpc_M") {
        testCircuit(new Forward) { dut =>
          dut.io.src.poke(1.U)
          dut.io.alu_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.alu_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.f_ctl.poke(HazardMacro.F_CTL_SNPC_M)
          dut.clock.step()
          dut.io.out.expect(6.U)
        }
      }
      test("wb_data") {
        testCircuit(new Forward) { dut =>
          dut.io.src.poke(1.U)
          dut.io.alu_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.alu_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.f_ctl.poke(HazardMacro.F_CTL_WB_DATA)
          dut.clock.step()
          dut.io.out.expect(7.U)
        }
      }
    }
  }
}

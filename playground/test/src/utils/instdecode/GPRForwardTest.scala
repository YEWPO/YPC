package utils.instdecode

import chisel3._
import chiseltest._
import utest._
import macros._

object GPRForwardTest extends ChiselUtestTester {
  val tests = Tests {
    test("Forward") {
      test("Default") {
        testCircuit(new GPRForward) { dut => dut.io.data1.poke(1.U)
          dut.io.data2.poke(1.U)
          dut.io.exe_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.exe_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.fa_ctl.poke(0.U)
          dut.io.fb_ctl.poke(0.U)
          dut.clock.step()
          dut.io.src1.expect(1.U)
          dut.io.src2.expect(1.U)
        }
      }
      test("exe_E") {
        testCircuit(new GPRForward) { dut =>
          dut.io.data1.poke(1.U)
          dut.io.data2.poke(1.U)
          dut.io.exe_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.exe_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.fa_ctl.poke(HazardMacros.F_CTL_EXE_E)
          dut.io.fb_ctl.poke(HazardMacros.F_CTL_EXE_E)
          dut.clock.step()
          dut.io.src1.expect(2.U)
          dut.io.src2.expect(2.U)
        }
      }
      test("snpc_E") {
        testCircuit(new GPRForward) { dut =>
          dut.io.data1.poke(1.U)
          dut.io.data2.poke(1.U)
          dut.io.exe_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.exe_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.fa_ctl.poke(HazardMacros.F_CTL_SNPC_E)
          dut.io.fb_ctl.poke(HazardMacros.F_CTL_SNPC_E)
          dut.clock.step()
          dut.io.src1.expect(3.U)
          dut.io.src2.expect(3.U)
        }
      }
      test("exe_M") {
        testCircuit(new GPRForward) { dut =>
          dut.io.data1.poke(1.U)
          dut.io.data2.poke(1.U)
          dut.io.exe_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.exe_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.fa_ctl.poke(HazardMacros.F_CTL_EXE_M)
          dut.io.fb_ctl.poke(HazardMacros.F_CTL_EXE_M)
          dut.clock.step()
          dut.io.src1.expect(4.U)
          dut.io.src2.expect(4.U)
        }
      }
      test("mem_M") {
        testCircuit(new GPRForward) { dut =>
          dut.io.data1.poke(1.U)
          dut.io.data2.poke(1.U)
          dut.io.exe_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.exe_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.fa_ctl.poke(HazardMacros.F_CTL_MEM_M)
          dut.io.fb_ctl.poke(HazardMacros.F_CTL_MEM_M)
          dut.clock.step()
          dut.io.src1.expect(5.U)
          dut.io.src2.expect(5.U)
        }
      }
      test("snpc_M") {
        testCircuit(new GPRForward) { dut =>
          dut.io.data1.poke(1.U)
          dut.io.data2.poke(1.U)
          dut.io.exe_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.exe_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.fa_ctl.poke(HazardMacros.F_CTL_SNPC_M)
          dut.io.fb_ctl.poke(HazardMacros.F_CTL_SNPC_M)
          dut.clock.step()
          dut.io.src1.expect(6.U)
          dut.io.src2.expect(6.U)
        }
      }
      test("wb_data") {
        testCircuit(new GPRForward) { dut =>
          dut.io.data1.poke(1.U)
          dut.io.data2.poke(1.U)
          dut.io.exe_E.poke(2.U)
          dut.io.snpc_E.poke(3.U)
          dut.io.exe_M.poke(4.U)
          dut.io.mem_M.poke(5.U)
          dut.io.snpc_M.poke(6.U)
          dut.io.wb_data.poke(7.U)
          dut.io.fa_ctl.poke(HazardMacros.F_CTL_WB_DATA)
          dut.io.fb_ctl.poke(HazardMacros.F_CTL_WB_DATA)
          dut.clock.step()
          dut.io.src1.expect(7.U)
          dut.io.src2.expect(7.U)
        }
      }
    }
  }
}

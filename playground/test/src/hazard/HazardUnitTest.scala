package hazard

import chisel3._
import chiseltest._
import utest._
import control._

object HazardUnitTest extends ChiselUtestTester {
  val tests = Tests {
    test("HazardUnit") {
      test("NoHazard") {
        testCircuit(new HazardUnit) { dut =>
          dut.inst_decode_hazard.rs1.poke(0.U)
          dut.inst_decode_hazard.rs2.poke(1.U)
          dut.execute_hazard.rd.poke(2.U)
          dut.execute_hazard.jump_sig.poke(false.B)
          dut.execute_hazard.wb_ctl.poke(ControlMacro.WB_CTL_ALU)
          dut.load_store_hazard.rd.poke(3.U)
          dut.load_store_hazard.wb_ctl.poke(ControlMacro.WB_CTL_MEM)
          dut.write_back_hazard.rd.poke(4.U)
          dut.clock.step()
          dut.inst_fetch_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.reset.expect(false.B)
          dut.execute_hazard.reset.expect(false.B)
          dut.inst_decode_hazard.fa_ctl.expect(HazardMacro.F_CTL_DEFAULT)
          dut.inst_decode_hazard.fb_ctl.expect(HazardMacro.F_CTL_DEFAULT)
        }
      }
      test("OneHazardOnEMem") {
        testCircuit(new HazardUnit) { dut =>
          dut.inst_decode_hazard.rs1.poke(11.U)
          dut.inst_decode_hazard.rs2.poke(12.U)
          dut.execute_hazard.rd.poke(11.U)
          dut.execute_hazard.jump_sig.poke(false.B)
          dut.execute_hazard.wb_ctl.poke(ControlMacro.WB_CTL_MEM)
          dut.load_store_hazard.rd.poke(13.U)
          dut.load_store_hazard.wb_ctl.poke(ControlMacro.WB_CTL_MEM)
          dut.write_back_hazard.rd.poke(14.U)
          dut.clock.step()
          dut.inst_fetch_hazard.enable.expect(false.B)
          dut.inst_decode_hazard.enable.expect(false.B)
          dut.inst_decode_hazard.reset.expect(false.B)
          dut.execute_hazard.reset.expect(true.B)
          dut.inst_decode_hazard.fa_ctl.expect(HazardMacro.F_CTL_DEFAULT)
          dut.inst_decode_hazard.fb_ctl.expect(HazardMacro.F_CTL_DEFAULT)
        }
      }
    }
  }
}

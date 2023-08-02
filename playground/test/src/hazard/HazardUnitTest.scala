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
          dut.inst_decode_hazard.rs1_tag.poke(true.B)
          dut.inst_decode_hazard.rs2_tag.poke(true.B)
          dut.execute_hazard.rd_tag.poke(true.B)
          dut.load_store_hazard.rd_tag.poke(true.B)
          dut.write_back_hazard.rd_tag.poke(true.B)
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
          dut.inst_decode_hazard.rs1_tag.poke(true.B)
          dut.inst_decode_hazard.rs2_tag.poke(true.B)
          dut.execute_hazard.rd_tag.poke(true.B)
          dut.load_store_hazard.rd_tag.poke(true.B)
          dut.write_back_hazard.rd_tag.poke(true.B)
          dut.clock.step()
          dut.inst_fetch_hazard.enable.expect(false.B)
          dut.inst_decode_hazard.enable.expect(false.B)
          dut.inst_decode_hazard.reset.expect(false.B)
          dut.execute_hazard.reset.expect(true.B)
          dut.inst_decode_hazard.fa_ctl.expect(HazardMacro.F_CTL_DEFAULT)
          dut.inst_decode_hazard.fb_ctl.expect(HazardMacro.F_CTL_DEFAULT)
        }
      }
      test("OneHazardOnE") {
        testCircuit(new HazardUnit) { dut =>
          dut.inst_decode_hazard.rs1.poke(21.U)
          dut.inst_decode_hazard.rs2.poke(22.U)
          dut.execute_hazard.rd.poke(22.U)
          dut.execute_hazard.jump_sig.poke(false.B)
          dut.execute_hazard.wb_ctl.poke(ControlMacro.WB_CTL_ALU)
          dut.load_store_hazard.rd.poke(23.U)
          dut.load_store_hazard.wb_ctl.poke(ControlMacro.WB_CTL_MEM)
          dut.write_back_hazard.rd.poke(14.U)
          dut.inst_decode_hazard.rs1_tag.poke(true.B)
          dut.inst_decode_hazard.rs2_tag.poke(true.B)
          dut.execute_hazard.rd_tag.poke(true.B)
          dut.load_store_hazard.rd_tag.poke(true.B)
          dut.write_back_hazard.rd_tag.poke(true.B)
          dut.clock.step()
          dut.inst_fetch_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.reset.expect(false.B)
          dut.execute_hazard.reset.expect(false.B)
          dut.inst_decode_hazard.fa_ctl.expect(HazardMacro.F_CTL_DEFAULT)
          dut.inst_decode_hazard.fb_ctl.expect(HazardMacro.F_CTL_ALU_E)
        }
      }
      test("OneHazardOnM") {
        testCircuit(new HazardUnit) { dut =>
          dut.inst_decode_hazard.rs1.poke(17.U)
          dut.inst_decode_hazard.rs2.poke(10.U)
          dut.execute_hazard.rd.poke(11.U)
          dut.execute_hazard.jump_sig.poke(false.B)
          dut.execute_hazard.wb_ctl.poke(ControlMacro.WB_CTL_ALU)
          dut.load_store_hazard.rd.poke(17.U)
          dut.load_store_hazard.wb_ctl.poke(ControlMacro.WB_CTL_MEM)
          dut.write_back_hazard.rd.poke(14.U)
          dut.inst_decode_hazard.rs1_tag.poke(true.B)
          dut.inst_decode_hazard.rs2_tag.poke(true.B)
          dut.execute_hazard.rd_tag.poke(true.B)
          dut.load_store_hazard.rd_tag.poke(true.B)
          dut.write_back_hazard.rd_tag.poke(true.B)
          dut.clock.step()
          dut.inst_fetch_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.reset.expect(false.B)
          dut.execute_hazard.reset.expect(false.B)
          dut.inst_decode_hazard.fa_ctl.expect(HazardMacro.F_CTL_MEM_M)
          dut.inst_decode_hazard.fb_ctl.expect(HazardMacro.F_CTL_DEFAULT)
        }
      }
      test("OneHazardOnW") {
        testCircuit(new HazardUnit) { dut =>
          dut.inst_decode_hazard.rs1.poke(7.U)
          dut.inst_decode_hazard.rs2.poke(2.U)
          dut.execute_hazard.rd.poke(11.U)
          dut.execute_hazard.jump_sig.poke(false.B)
          dut.execute_hazard.wb_ctl.poke(ControlMacro.WB_CTL_MEM)
          dut.load_store_hazard.rd.poke(17.U)
          dut.load_store_hazard.wb_ctl.poke(ControlMacro.WB_CTL_SNPC)
          dut.write_back_hazard.rd.poke(2.U)
          dut.inst_decode_hazard.rs1_tag.poke(true.B)
          dut.inst_decode_hazard.rs2_tag.poke(true.B)
          dut.execute_hazard.rd_tag.poke(true.B)
          dut.load_store_hazard.rd_tag.poke(true.B)
          dut.write_back_hazard.rd_tag.poke(true.B)
          dut.clock.step()
          dut.inst_fetch_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.reset.expect(false.B)
          dut.execute_hazard.reset.expect(false.B)
          dut.inst_decode_hazard.fa_ctl.expect(HazardMacro.F_CTL_DEFAULT)
          dut.inst_decode_hazard.fb_ctl.expect(HazardMacro.F_CTL_WB_DATA)
        }
      }
      test("MultiHazard") {
        testCircuit(new HazardUnit) { dut =>
          dut.inst_decode_hazard.rs1.poke(31.U)
          dut.inst_decode_hazard.rs2.poke(5.U)
          dut.execute_hazard.rd.poke(11.U)
          dut.execute_hazard.jump_sig.poke(false.B)
          dut.execute_hazard.wb_ctl.poke(ControlMacro.WB_CTL_MEM)
          dut.load_store_hazard.rd.poke(31.U)
          dut.load_store_hazard.wb_ctl.poke(ControlMacro.WB_CTL_SNPC)
          dut.write_back_hazard.rd.poke(31.U)
          dut.inst_decode_hazard.rs1_tag.poke(true.B)
          dut.inst_decode_hazard.rs2_tag.poke(true.B)
          dut.execute_hazard.rd_tag.poke(true.B)
          dut.load_store_hazard.rd_tag.poke(true.B)
          dut.write_back_hazard.rd_tag.poke(true.B)
          dut.clock.step()
          dut.inst_fetch_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.reset.expect(false.B)
          dut.execute_hazard.reset.expect(false.B)
          dut.inst_decode_hazard.fa_ctl.expect(HazardMacro.F_CTL_SNPC_M)
          dut.inst_decode_hazard.fb_ctl.expect(HazardMacro.F_CTL_DEFAULT)
        }
      }
      test("BothHazardDiff") {
        testCircuit(new HazardUnit) { dut =>
          dut.inst_decode_hazard.rs1.poke(13.U)
          dut.inst_decode_hazard.rs2.poke(15.U)
          dut.execute_hazard.rd.poke(13.U)
          dut.execute_hazard.jump_sig.poke(false.B)
          dut.execute_hazard.wb_ctl.poke(ControlMacro.WB_CTL_SNPC)
          dut.load_store_hazard.rd.poke(31.U)
          dut.load_store_hazard.wb_ctl.poke(ControlMacro.WB_CTL_SNPC)
          dut.write_back_hazard.rd.poke(15.U)
          dut.inst_decode_hazard.rs1_tag.poke(true.B)
          dut.inst_decode_hazard.rs2_tag.poke(true.B)
          dut.execute_hazard.rd_tag.poke(true.B)
          dut.load_store_hazard.rd_tag.poke(true.B)
          dut.write_back_hazard.rd_tag.poke(true.B)
          dut.clock.step()
          dut.inst_fetch_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.reset.expect(false.B)
          dut.execute_hazard.reset.expect(false.B)
          dut.inst_decode_hazard.fa_ctl.expect(HazardMacro.F_CTL_SNPC_E)
          dut.inst_decode_hazard.fb_ctl.expect(HazardMacro.F_CTL_WB_DATA)
        }
      }
      test("BothHazardDiffMem") {
        testCircuit(new HazardUnit) { dut =>
          dut.inst_decode_hazard.rs1.poke(13.U)
          dut.inst_decode_hazard.rs2.poke(15.U)
          dut.execute_hazard.rd.poke(13.U)
          dut.execute_hazard.jump_sig.poke(false.B)
          dut.execute_hazard.wb_ctl.poke(ControlMacro.WB_CTL_MEM)
          dut.load_store_hazard.rd.poke(31.U)
          dut.load_store_hazard.wb_ctl.poke(ControlMacro.WB_CTL_SNPC)
          dut.write_back_hazard.rd.poke(15.U)
          dut.inst_decode_hazard.rs1_tag.poke(true.B)
          dut.inst_decode_hazard.rs2_tag.poke(true.B)
          dut.execute_hazard.rd_tag.poke(true.B)
          dut.load_store_hazard.rd_tag.poke(true.B)
          dut.write_back_hazard.rd_tag.poke(true.B)
          dut.clock.step()
          dut.inst_fetch_hazard.enable.expect(false.B)
          dut.inst_decode_hazard.enable.expect(false.B)
          dut.inst_decode_hazard.reset.expect(false.B)
          dut.execute_hazard.reset.expect(true.B)
          dut.inst_decode_hazard.fa_ctl.expect(HazardMacro.F_CTL_DEFAULT)
          dut.inst_decode_hazard.fb_ctl.expect(HazardMacro.F_CTL_WB_DATA)
        }
      }
      test("BothHazardSame") {
        testCircuit(new HazardUnit) { dut =>
          dut.inst_decode_hazard.rs1.poke(15.U)
          dut.inst_decode_hazard.rs2.poke(15.U)
          dut.execute_hazard.rd.poke(11.U)
          dut.execute_hazard.jump_sig.poke(false.B)
          dut.execute_hazard.wb_ctl.poke(ControlMacro.WB_CTL_ALU)
          dut.load_store_hazard.rd.poke(15.U)
          dut.load_store_hazard.wb_ctl.poke(ControlMacro.WB_CTL_ALU)
          dut.write_back_hazard.rd.poke(9.U)
          dut.inst_decode_hazard.rs1_tag.poke(true.B)
          dut.inst_decode_hazard.rs2_tag.poke(true.B)
          dut.execute_hazard.rd_tag.poke(true.B)
          dut.load_store_hazard.rd_tag.poke(true.B)
          dut.write_back_hazard.rd_tag.poke(true.B)
          dut.clock.step()
          dut.inst_fetch_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.reset.expect(false.B)
          dut.execute_hazard.reset.expect(false.B)
          dut.inst_decode_hazard.fa_ctl.expect(HazardMacro.F_CTL_ALU_M)
          dut.inst_decode_hazard.fb_ctl.expect(HazardMacro.F_CTL_ALU_M)
        }
      }
      test("ZeroHazard") {
        testCircuit(new HazardUnit) { dut =>
          dut.inst_decode_hazard.rs1.poke(0.U)
          dut.inst_decode_hazard.rs2.poke(15.U)
          dut.execute_hazard.rd.poke(0.U)
          dut.execute_hazard.jump_sig.poke(false.B)
          dut.execute_hazard.wb_ctl.poke(ControlMacro.WB_CTL_ALU)
          dut.load_store_hazard.rd.poke(0.U)
          dut.load_store_hazard.wb_ctl.poke(ControlMacro.WB_CTL_ALU)
          dut.write_back_hazard.rd.poke(0.U)
          dut.inst_decode_hazard.rs1_tag.poke(true.B)
          dut.inst_decode_hazard.rs2_tag.poke(true.B)
          dut.execute_hazard.rd_tag.poke(true.B)
          dut.load_store_hazard.rd_tag.poke(true.B)
          dut.write_back_hazard.rd_tag.poke(true.B)
          dut.clock.step()
          dut.inst_fetch_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.reset.expect(false.B)
          dut.execute_hazard.reset.expect(false.B)
          dut.inst_decode_hazard.fa_ctl.expect(HazardMacro.F_CTL_DEFAULT)
          dut.inst_decode_hazard.fb_ctl.expect(HazardMacro.F_CTL_DEFAULT)
        }
      }
      test("Jump") {
        testCircuit(new HazardUnit) { dut =>
          dut.inst_decode_hazard.rs1.poke(31.U)
          dut.inst_decode_hazard.rs2.poke(5.U)
          dut.execute_hazard.rd.poke(11.U)
          dut.execute_hazard.jump_sig.poke(true.B)
          dut.execute_hazard.wb_ctl.poke(ControlMacro.WB_CTL_MEM)
          dut.load_store_hazard.rd.poke(31.U)
          dut.load_store_hazard.wb_ctl.poke(ControlMacro.WB_CTL_SNPC)
          dut.write_back_hazard.rd.poke(31.U)
          dut.inst_decode_hazard.rs1_tag.poke(true.B)
          dut.inst_decode_hazard.rs2_tag.poke(true.B)
          dut.execute_hazard.rd_tag.poke(true.B)
          dut.load_store_hazard.rd_tag.poke(true.B)
          dut.write_back_hazard.rd_tag.poke(true.B)
          dut.clock.step()
          dut.inst_fetch_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.enable.expect(true.B)
          dut.inst_decode_hazard.reset.expect(true.B)
          dut.execute_hazard.reset.expect(true.B)
          dut.inst_decode_hazard.fa_ctl.expect(HazardMacro.F_CTL_SNPC_M)
          dut.inst_decode_hazard.fb_ctl.expect(HazardMacro.F_CTL_DEFAULT)
        }
      }
    }
  }
}

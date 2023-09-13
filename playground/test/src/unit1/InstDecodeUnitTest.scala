package unit

import chisel3._
import chiseltest._
import utest._
import control._

object InstDecodeUnitTest extends ChiselUtestTester {
  val tests = Tests {
    test("InstDecodeUnit") {
      test("ResetOn") {
        testCircuit(new InstDecodeUnit) { dut =>
          dut.inst_fetch_data.pc.poke("h10001073".U)
          dut.inst_decode_hazard.enable.poke(true.B)
          dut.inst_decode_hazard.reset.poke(true.B)
          dut.clock.step()
          dut.inst_decode_data.pc.expect("h80000000".U)
        }
      }
      test("ResetOff") {
        testCircuit(new InstDecodeUnit) { dut =>
          dut.inst_fetch_data.pc.poke("h10001073".U)
          dut.inst_decode_hazard.enable.poke(true.B)
          dut.inst_decode_hazard.reset.poke(false.B)
          dut.clock.step()
          dut.inst_decode_data.pc.expect("h10001073".U)
        }
      }
      test("Disable") {
        testCircuit(new InstDecodeUnit) { dut =>
          dut.inst_fetch_data.pc.poke("h10001073".U)
          dut.inst_decode_hazard.enable.poke(true.B)
          dut.inst_decode_hazard.reset.poke(false.B)
          dut.clock.step()
          dut.inst_decode_data.pc.expect("h10001073".U)

          dut.inst_fetch_data.pc.poke("h10001023".U)
          dut.inst_decode_hazard.enable.poke(false.B)
          dut.inst_decode_hazard.reset.poke(false.B)
          dut.clock.step()
          dut.inst_decode_data.pc.expect("h10001073".U)
        }
      }
      test("integrated") {
        testCircuit(new InstDecodeUnit) { dut =>
          dut.write_back_data.rd.poke(2.U)
          dut.write_back_data.wb_data.poke("h80000738".U)
          dut.write_back_control.reg_w_en.poke(true.B)
          dut.clock.step()

          dut.inst_fetch_data.inst.poke("h02813083".U)
          dut.inst_decode_hazard.enable.poke(true.B)
          dut.inst_decode_hazard.reset.poke(false.B)
          dut.clock.step()
          dut.inst_decode_data.imm.expect(40.U)
          dut.inst_decode_data.src1.expect("h80000738".U)
          dut.inst_decode_control.b_ctl.expect(ControlMacro.B_CTL_IMM)
          dut.inst_decode_control.dnpc_ctl.expect(ControlMacro.DNPC_CTL_DEFAULT)
          dut.inst_decode_control.wb_ctl.expect(ControlMacro.WB_CTL_MEM)
          dut.inst_decode_control.jump_op.expect(ControlMacro.JUMP_OP_DEFAULT)
          dut.inst_decode_control.alu_ctl.expect(ControlMacro.ALU_CTL_ADD)
          dut.inst_decode_control.invalid_op.expect(false.B)
          dut.inst_decode_control.ebreak_op.expect(false.B)
        }
      }
    }
  }
}

package utils

import chisel3._
import chiseltest._
import utest._
import control._

object ImmGenTest extends ChiselUtestTester {
  val tests = Tests {
    test("ImmGen") {
      test("SignedExtend") {
        // also test I-type
        testCircuit(new ImmGen()) { dut =>
          dut.io.in.poke("h1ff8202".U(25.W));
          dut.io.imm_type.poke(ControlMacro.IMM_TYPE_I)
          dut.clock.step()
          dut.io.imm_out.expect("hfffffffffffffffc".U(64.W))
        }
      }
      test("S-type") {
        testCircuit(new ImmGen()) { dut =>
          dut.io.in.poke("h82268".U(25.W));
          dut.io.imm_type.poke(ControlMacro.IMM_TYPE_S)
          dut.clock.step()
          dut.io.imm_out.expect("h48".U(64.W))
        }
      }
      test("B-type") {
        testCircuit(new ImmGen()) { dut =>
          dut.io.in.poke("h1fd1321".U(25.W));
          dut.io.imm_type.poke(ControlMacro.IMM_TYPE_B)
          dut.clock.step()
          dut.io.imm_out.expect("hffffffffffffffe0".U(64.W))
        }
      }
      test("U-type") {
        testCircuit(new ImmGen()) { dut =>
          dut.io.in.poke("h122".U(25.W));
          dut.io.imm_type.poke(ControlMacro.IMM_TYPE_U)
          dut.clock.step()
          dut.io.imm_out.expect("h9000".U(64.W))
        }
      }
      test("J-type") {
        testCircuit(new ImmGen()) { dut =>
          dut.io.in.poke("h1eebfe1".U(25.W));
          dut.io.imm_type.poke(ControlMacro.IMM_TYPE_J)
          dut.clock.step()
          dut.io.imm_out.expect("hffffffffffffff74".U(64.W))
        }
      }
    }
  }
}

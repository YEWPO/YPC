package utils

import chisel3._
import chiseltest._
import utest._
import control._

object AlgLogTest extends ChiselUtestTester {
  val tests = Tests {
    test("AlgorithmLogicUnit") {
      test("sra") {
        testCircuit(new AlgLog) { dut =>
          dut.io.src1.poke("h8000_0000_0000_0000".U)
          dut.io.src2.poke(2.U)
          dut.io.alu_ctl.poke(ControlMacro.ALU_CTL_SRA)
          dut.clock.step()
          dut.io.alu_out.expect("he000_0000_0000_0000".U)
        }
      }
    }
  }
}

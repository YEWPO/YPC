package register

import chisel3._
import chiseltest._
import utest._

object SelectorPCTest extends ChiselUtestTester {
  val tests = Tests {
    test("jal") {
      testCircuit(new SelectorPC) { dut =>
        dut.io.opcode.poke("b1101111".U)
        dut.io.branch_out.poke(false.B)
        dut.io.alu_out.poke("h02".U)
        dut.io.snpc.poke("h8".U)
        dut.clock.step()
        dut.io.next_pc.expect("h02".U)
      }
    }
    test("jalr") {
      testCircuit(new SelectorPC) { dut =>
        dut.io.opcode.poke("b1100111".U)
        dut.io.branch_out.poke(false.B)
        dut.io.alu_out.poke("h3".U)
        dut.io.snpc.poke("hc".U)
        dut.clock.step()
        dut.io.next_pc.expect("h2".U)
      }
    }
  }
}

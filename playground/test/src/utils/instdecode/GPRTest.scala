package utils.instdecode

import chisel3._
import chiseltest._
import utest._

object GPRTest extends ChiselUtestTester {
  val tests = Tests {
    test("GPR") {
      test("readRS1") {
        testCircuit(new GPR) { dut =>
          // init, write 0x1234 to x1, read x1 from rs1
          dut.io.rd.poke("h1".U)
          dut.io.rs1.poke("h1".U)
          dut.io.rs2.poke("h0".U)
          dut.io.w_en.poke(true.B)
          dut.io.w_data.poke("h1234".U)
          dut.clock.step()
          dut.io.r_data1.expect("h1234".U)
          dut.io.r_data2.expect("h0".U)
        }
      }
      test("readRS2") {
        testCircuit(new GPR) { dut =>
          // init, write 0x13456 to x2, read x2 from rs2
          dut.io.rd.poke("h2".U)
          dut.io.rs1.poke("h0".U)
          dut.io.rs2.poke("h2".U)
          dut.io.w_en.poke(true.B)
          dut.io.w_data.poke("h13456".U)
          dut.clock.step()
          dut.io.r_data1.expect("h0".U)
          dut.io.r_data2.expect("h13456".U)
        }
      }
      test("writeRD") {
        testCircuit(new GPR) { dut =>
          // init, write 0x11 to x17, and try to read x17 at this time
          dut.io.rd.poke("h11".U)
          dut.io.rs1.poke("h11".U)
          dut.io.rs2.poke("h0".U)
          dut.io.w_en.poke(true.B)
          dut.io.w_data.poke("h11".U)
          dut.clock.step()
          dut.io.r_data1.expect("h11".U)
          dut.io.r_data2.expect("h0".U)
        }
      }
      test("writeRD2times") {
        testCircuit(new GPR) { dut =>
          // init, write 0xffff to x10, read x10 from both rs1 and rs2
          dut.io.rd.poke("ha".U)
          dut.io.rs1.poke("ha".U)
          dut.io.rs2.poke("ha".U)
          dut.io.w_en.poke(true.B)
          dut.io.w_data.poke("hffff".U)
          dut.clock.step()
          dut.io.r_data1.expect("hffff".U)
          dut.io.r_data2.expect("hffff".U)

          // disable write 0x7890 to x10 at this time, read x10 from both rs1 and rs2
          dut.io.rd.poke("ha".U)
          dut.io.rs1.poke("ha".U)
          dut.io.rs2.poke("ha".U)
          dut.io.w_en.poke(false.B)
          dut.io.w_data.poke("h7890".U)
          dut.clock.step()
          dut.io.r_data1.expect("hffff".U)
          dut.io.r_data2.expect("hffff".U)
        }
      }
      test("writeZero") {
        testCircuit(new GPR) { dut =>
          // init, write 0xcdcd to x0
          dut.io.rd.poke(0.U)
          dut.io.rs1.poke(0.U)
          dut.io.rs2.poke(0.U)
          dut.io.w_en.poke(true.B)
          dut.io.w_data.poke("hcdcd".U)
          dut.clock.step()
          dut.io.r_data1.expect(0.U)
          dut.io.r_data2.expect(0.U)
        }
      }
    }
  }
}

import chisel3._
import chiseltest._
import utest._

object RegsTest extends ChiselUtestTester {
  val tests = Tests {
    test("zero") {
      testCircuit(new Regs) {
        dut =>
          dut.io.data.poke(12.U)
          dut.io.rd.poke(0.U)
          dut.clock.step()
          dut.io.regOut(0).expect(0.U)
      }
    }
    test("others") {
      testCircuit(new Regs) {
        dut =>
          dut.io.data.poke(12.U)
          dut.io.rd.poke(1.U)
          dut.clock.step()
          dut.io.regOut(1).expect(12.U)
          dut.io.data.poke(100.U)
          dut.io.rd.poke(2.U)
          dut.clock.step()
          dut.io.regOut(0).expect(0.U)
          dut.io.regOut(1).expect(12.U)
          dut.io.regOut(2).expect(100.U)
          dut.io.regOut(3).expect(0.U)
          
          dut.io.data.poke(123.U)
          dut.io.rd.poke(31.U)
          dut.clock.step()
          dut.io.regOut(31).expect(123.U)
      }
    }
  }
}

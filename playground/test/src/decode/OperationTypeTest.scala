package decode

import chisel3._
import chiseltest._
import utest._

object OperationTypeTest extends ChiselUtestTester {
  val tests = Tests {
    test("OperationType") {
      testCircuit(new OperationType) {
        dut =>
          dut.io.operation.poke("b0110111".U)
          dut.clock.step()
          dut.io.operation_type.expect("b100".U)

          dut.io.operation.poke("b0010111".U)
          dut.clock.step()
          dut.io.operation_type.expect("b100".U)

          dut.io.operation.poke("b1101111".U)
          dut.clock.step()
          dut.io.operation_type.expect("b101".U)

          dut.io.operation.poke("b1100111".U)
          dut.clock.step()
          dut.io.operation_type.expect("b001".U)
      }
    }
  }
}

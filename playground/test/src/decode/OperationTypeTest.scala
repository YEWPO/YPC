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
          dut.io.word_op.expect(false.B)

          dut.io.operation.poke("b0010111".U)
          dut.clock.step()
          dut.io.operation_type.expect("b100".U)
          dut.io.word_op.expect(false.B)

          dut.io.operation.poke("b1101111".U)
          dut.clock.step()
          dut.io.operation_type.expect("b101".U)
          dut.io.word_op.expect(false.B)

          dut.io.operation.poke("b1100111".U)
          dut.clock.step()
          dut.io.operation_type.expect("b001".U)
          dut.io.word_op.expect(false.B)
      }
    }
    test("word_op") {
      testCircuit(new OperationType) {
        dut =>
          dut.io.operation.poke("b0111011".U)
          dut.clock.step()
          dut.io.operation_type.expect("b000".U)
          dut.io.word_op.expect(true.B)

          dut.io.operation.poke("b0010111".U)
          dut.clock.step()
          dut.io.operation_type.expect("b100".U)
          dut.io.word_op.expect(false.B)

          dut.io.operation.poke("b0011011".U)
          dut.clock.step()
          dut.io.operation_type.expect("b001".U)
          dut.io.word_op.expect(true.B)
      }
    }
  }
}

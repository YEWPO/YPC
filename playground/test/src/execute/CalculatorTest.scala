package execute

import chisel3._
import chiseltest._
import utest._

object CalculatorTest extends ChiselUtestTester {
  val tests = Tests {
    test("calculator") {
      test("64") {
        test("add") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke(1.U)
              dut.io.src2.poke(1.U)
              dut.io.funct.poke(0.U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(false.B)
              dut.clock.step()
              dut.io.res.expect(2.U)
          }
        }
        test("sub") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke(1.U)
              dut.io.src2.poke(1.U)
              dut.io.funct.poke(0.U)
              dut.io.subop_type.poke(true.B)
              dut.io.word_op.poke(false.B)
              dut.clock.step()
              dut.io.res.expect(0.U)
          }
        }
        test("sll") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke(1.U)
              dut.io.src2.poke(1.U)
              dut.io.funct.poke(1.U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(false.B)
              dut.clock.step()
              dut.io.res.expect(2.U)
          }
        }
        test("slt") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("hffffffffffffffff".U)
              dut.io.src2.poke(2.U)
              dut.io.funct.poke(2.U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(false.B)
              dut.clock.step()
              dut.io.res.expect(1.U)
          }
        }
        test("sltu") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("hffffffffffffffff".U)
              dut.io.src2.poke(2.U)
              dut.io.funct.poke(3.U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(false.B)
              dut.clock.step()
              dut.io.res.expect(0.U)
          }
        }
        test("xor") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("b1100".U)
              dut.io.src2.poke("b1010".U)
              dut.io.funct.poke(4.U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(false.B)
              dut.clock.step()
              dut.io.res.expect("b0110".U)
          }
        }
        test("srl") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("hffffffffffffffff".U)
              dut.io.src2.poke("b100001".U)
              dut.io.funct.poke(5.U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(false.B)
              dut.clock.step()
              dut.io.res.expect("h7fffffff".U)
          }
        }
        test("sra") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("hffffffffffffffff".U)
              dut.io.src2.poke("b100001".U)
              dut.io.funct.poke(5.U)
              dut.io.subop_type.poke(true.B)
              dut.io.word_op.poke(false.B)
              dut.clock.step()
              dut.io.res.expect("hffffffffffffffff".U)
          }
        }
        test("or") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("b1100".U)
              dut.io.src2.poke("b1010".U)
              dut.io.funct.poke(6.U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(false.B)
              dut.clock.step()
              dut.io.res.expect("b1110".U)
          }
        }
        test("and") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("b1100".U)
              dut.io.src2.poke("b1010".U)
              dut.io.funct.poke(7.U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(false.B)
              dut.clock.step()
              dut.io.res.expect("b1000".U)
          }
        }
      }
      test("32") {
        test("add") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("hf0000000_ffffffff".U)
              dut.io.src2.poke("hf0000000_00000001".U)
              dut.io.funct.poke("b000".U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(true.B)
              dut.clock.step()
              dut.io.res.expect("h00000000_00000000".U)
          }
        }
        test("sub") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("hf0000000_ffffffff".U)
              dut.io.src2.poke("hf0000000_00000001".U)
              dut.io.funct.poke("b000".U)
              dut.io.subop_type.poke(true.B)
              dut.io.word_op.poke(true.B)
              dut.clock.step()
              dut.io.res.expect("hffffffff_fffffffe".U)
          }
        }
        test("sll") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("hf0000000_ffffffff".U)
              dut.io.src2.poke("hf0000000_00000001".U)
              dut.io.funct.poke("b001".U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(true.B)
              dut.clock.step()
              dut.io.res.expect("hffffffff_fffffffe".U)
          }
        }
        test("xor") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("hf0000000_ffffffff".U)
              dut.io.src2.poke("hf0000000_00000001".U)
              dut.io.funct.poke("b100".U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(true.B)
              dut.clock.step()
              dut.io.res.expect("hffffffff_fffffffe".U)
          }
        }
        test("srl") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("hf0000000_ffffffff".U)
              dut.io.src2.poke("hf0000000_00000001".U)
              dut.io.funct.poke("b101".U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(true.B)
              dut.clock.step()
              dut.io.res.expect("h00000000_7fffffff".U)
          }
        }
        test("sra") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("hf0000000_ffffffff".U)
              dut.io.src2.poke("hf0000000_00000001".U)
              dut.io.funct.poke("b101".U)
              dut.io.subop_type.poke(true.B)
              dut.io.word_op.poke(true.B)
              dut.clock.step()
              dut.io.res.expect("hffffffff_ffffffff".U)
          }
        }
        test("or") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("hf0000000_ffffffff".U)
              dut.io.src2.poke("hf0000000_00000001".U)
              dut.io.funct.poke("b110".U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(true.B)
              dut.clock.step()
              dut.io.res.expect("hffffffff_ffffffff".U)
          }
        }
        test("and") {
          testCircuit(new Calculator) {
            dut =>
              dut.io.src1.poke("hf0000000_ffffffff".U)
              dut.io.src2.poke("hf0000000_00000001".U)
              dut.io.funct.poke("b111".U)
              dut.io.subop_type.poke(false.B)
              dut.io.word_op.poke(true.B)
              dut.clock.step()
              dut.io.res.expect("h00000000_00000001".U)
          }
        }
      }
    }
  }
}

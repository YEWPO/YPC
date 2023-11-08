package utils.execute

import chisel3._
import utest._
import chiseltest._
import java.util.Random

object MulTest extends ChiselUtestTester {
  val tests = Tests {
    test("gen part summand") {
      def testGenPartSummand(len: Int, src: Int, booth: Int) : Unit = {
        print("len: " + len + ", src: " + src + ", booth: " + booth)

        var ans = booth match {
          case 0 => 0
          case 1 => src
          case 2 => src
          case 3 => (src << 1)
          case 4 => ~(src << 1)
          case 5 => ~src
          case 6 => ~src
          case 7 => 0
          case _: Int => 0
        }

        ans = ans & ((1 << len) - 1)

        val carry = (booth >= 4) && (booth < 7)

        testCircuit(new GenPartSummand(len)) { dut =>
          dut.io.src.poke(src.U)
          dut.io.booth.poke(booth.U)
          dut.clock.step()
          dut.io.carry.expect(carry.B)
          dut.io.summand.expect(ans.U)
        }

        println(" Test success!")
      }

      val rand = new Random

      for (i <- 1 to 100) {
        val len = rand.nextInt(16) + 1
        val src = rand.nextInt((1 << len) - 1)
        val booth = rand.nextInt(8)
        testGenPartSummand(len, src, booth)
      }
    }
  }
}

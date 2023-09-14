package bundles

import chisel3._

class StatisticBundle extends Bundle {
  val ebreak_op  = Bool()
  val invalid_op = Bool()
  val pc         = UInt(64.W)
  val inst       = UInt(32.W)
  val dnpc       = UInt(64.W)
}

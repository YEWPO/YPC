package utils.writeback

import chisel3._
import bundles._

class StatisticIO extends Bundle {
  val in = Input(new StatisticBundle)
}

class Statistic extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new StatisticIO)

  /* ========== Module ========== */
  val except     = Module(new Except)
  val debug_info = Module(new DebugInfo)

  /* ========== Combinational Circuit ========== */
  except.io.ebreak_op  := io.in.ebreak_op
  except.io.invalid_op := io.in.invalid_op
  except.io.pc         := io.in.pc

  debug_info.io.pc   := io.in.pc
  debug_info.io.inst := io.in.inst
  debug_info.io.dnpc := io.in.dnpc
}

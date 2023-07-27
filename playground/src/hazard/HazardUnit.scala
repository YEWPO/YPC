package hazard

import chisel3._

class HazardUnitIO extends Bundle {
  val enable_f = Output(Bool())

  val enable_d = Output(Bool())
  val reset_d  = Output(Bool())
}

class HazardUnit extends Module {
  val io = IO(new HazardUnitIO)

  io.enable_f := true.B

  io.enable_d := true.B
  io.reset_d  := false.B
}

package control

import chisel3._

class ControlUnitIO extends Bundle {
  val inst = Input(UInt(64.W))

  val imm_type = Output(UInt(3.W))
}

class ControlUnit extends Module {
  val io = IO(new ControlUnitIO())
}

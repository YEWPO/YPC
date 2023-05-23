package decode

import chisel3._

class TypeOH extends Module {
  val io = IO(new Bundle {
    val op_type = Input(UInt(3.W))
    
    val R_type = Output(Bool())
    val I_type = Output(Bool())
    val S_type = Output(Bool())
    val B_type = Output(Bool())
    val U_type = Output(Bool())
    val J_type = Output(Bool())
  })

  io.R_type := Mux(io.op_type === "b000".U, true.B, false.B)
  io.I_type := Mux(io.op_type === "b001".U, true.B, false.B)
  io.S_type := Mux(io.op_type === "b010".U, true.B, false.B)
  io.B_type := Mux(io.op_type === "b011".U, true.B, false.B)
  io.U_type := Mux(io.op_type === "b100".U, true.B, false.B)
  io.J_type := Mux(io.op_type === "b101".U, true.B, false.B)
}

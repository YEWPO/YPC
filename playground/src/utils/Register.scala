package utils

import chisel3._

class RegisterIO extends Bundle {
  val rs1 = Input(UInt(5.W))
  val rs2 = Input(UInt(5.W))
  val rd = Input(UInt(5.W))

  val w_en = Input(Bool())
  val w_data = Input(UInt(64.W))

  val r_data1 = Output(UInt(64.W))
  val r_data2 = Output(UInt(64.W))
}

class Register extends Module {
  val io = IO(new RegisterIO())

  val reg_files = RegInit(VecInit(Seq.fill(32)(0.U(64.W))))

  reg_files(io.rd) := Mux(io.rd.orR && io.w_en, io.w_data, reg_files(io.rd))

  io.r_data1 := reg_files(io.rs1)
  io.r_data2 := reg_files(io.rs2)
}

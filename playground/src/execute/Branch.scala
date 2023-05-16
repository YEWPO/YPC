package execute

import chisel3._
import chisel3.util._

class Branch extends Module {
  val io = IO(new Bundle {
    val src1 = Input(UInt(64.W))
    val src2 = Input(UInt(64.W))
    val funct = Input(UInt(3.W))
    val branch_en = Input(Bool())
    val res = Output(Bool())
  })

  val equal = Module(new Equal)
  val slt = Module(new Slt)
  val sltu = Module(new Sltu)

  equal.io.src1 := io.src1
  equal.io.src2 := io.src2

  slt.io.src1 := io.src1
  slt.io.src2 := io.src2

  sltu.io.src1 := io.src1
  sltu.io.src2 := io.src2

  val res = MuxCase(false.B, Array (
    (io.funct === "b000".U) -> Mux(equal.io.res === 1.U, true.B, false.B),
    (io.funct === "b001".U) -> Mux(equal.io.res === 0.U, true.B, false.B),
    (io.funct === "b100".U) -> Mux(slt.io.res === 1.U, true.B, false.B),
    (io.funct === "b101".U) -> Mux(slt.io.res === 0.U, true.B, false.B),
    (io.funct === "b110".U) -> Mux(sltu.io.res === 1.U, true.B, false.B),
    (io.funct === "b111".U) -> Mux(sltu.io.res === 0.U, true.B, false.B)
    ))

  io.res := Mux(io.branch_en, res, false.B)
}

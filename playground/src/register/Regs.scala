package register

import chisel3._
import chisel3.util._

class Regs extends Module {
  val io = IO(new Bundle {
    val w_en = Input(Bool())
    val w_data = Input(UInt(64.W))
    val w_rd = Input(UInt(5.W))
    val r_rs1 = Input(UInt(5.W))
    val r_rs2 = Input(UInt(5.W))
    val r_data1 = Output(UInt(64.W))
    val r_data2 = Output(UInt(64.W))
  })

  val regFile = RegInit(VecInit(Seq.fill(32)(0.U(64.W))))
  val regs2cpp = Module(new Regs2cpp)

  regs2cpp.io.regs := regFile

  for (i <- 1 to 31) {
    regFile(i) := Mux((io.w_rd === i.asUInt) && io.w_en, io.w_data, regFile(i))
  }

  io.r_data1 := MuxCase(0.U(64.W), Array(
    (io.r_rs1 === 0.U(5.W)) -> regFile(0),
    (io.r_rs1 === 1.U(5.W)) -> regFile(1),
    (io.r_rs1 === 2.U(5.W)) -> regFile(2),
    (io.r_rs1 === 3.U(5.W)) -> regFile(3),
    (io.r_rs1 === 4.U(5.W)) -> regFile(4),
    (io.r_rs1 === 5.U(5.W)) -> regFile(5),
    (io.r_rs1 === 6.U(5.W)) -> regFile(6),
    (io.r_rs1 === 7.U(5.W)) -> regFile(7),
    (io.r_rs1 === 8.U(5.W)) -> regFile(8),
    (io.r_rs1 === 9.U(5.W)) -> regFile(9),
    (io.r_rs1 === 10.U(5.W)) -> regFile(10),
    (io.r_rs1 === 11.U(5.W)) -> regFile(11),
    (io.r_rs1 === 12.U(5.W)) -> regFile(12),
    (io.r_rs1 === 13.U(5.W)) -> regFile(13),
    (io.r_rs1 === 14.U(5.W)) -> regFile(14),
    (io.r_rs1 === 15.U(5.W)) -> regFile(15),
    (io.r_rs1 === 16.U(5.W)) -> regFile(16),
    (io.r_rs1 === 17.U(5.W)) -> regFile(17),
    (io.r_rs1 === 18.U(5.W)) -> regFile(18),
    (io.r_rs1 === 19.U(5.W)) -> regFile(19),
    (io.r_rs1 === 20.U(5.W)) -> regFile(20),
    (io.r_rs1 === 21.U(5.W)) -> regFile(21),
    (io.r_rs1 === 22.U(5.W)) -> regFile(22),
    (io.r_rs1 === 23.U(5.W)) -> regFile(23),
    (io.r_rs1 === 24.U(5.W)) -> regFile(24),
    (io.r_rs1 === 25.U(5.W)) -> regFile(25),
    (io.r_rs1 === 26.U(5.W)) -> regFile(26),
    (io.r_rs1 === 27.U(5.W)) -> regFile(27),
    (io.r_rs1 === 28.U(5.W)) -> regFile(28),
    (io.r_rs1 === 29.U(5.W)) -> regFile(29),
    (io.r_rs1 === 30.U(5.W)) -> regFile(30),
    (io.r_rs1 === 31.U(5.W)) -> regFile(31)
    ))

  io.r_data2 := MuxCase(0.U(64.W), Array(
    (io.r_rs2 === 0.U(5.W)) -> regFile(0),
    (io.r_rs2 === 1.U(5.W)) -> regFile(1),
    (io.r_rs2 === 2.U(5.W)) -> regFile(2),
    (io.r_rs2 === 3.U(5.W)) -> regFile(3),
    (io.r_rs2 === 4.U(5.W)) -> regFile(4),
    (io.r_rs2 === 5.U(5.W)) -> regFile(5),
    (io.r_rs2 === 6.U(5.W)) -> regFile(6),
    (io.r_rs2 === 7.U(5.W)) -> regFile(7),
    (io.r_rs2 === 8.U(5.W)) -> regFile(8),
    (io.r_rs2 === 9.U(5.W)) -> regFile(9),
    (io.r_rs2 === 10.U(5.W)) -> regFile(10),
    (io.r_rs2 === 11.U(5.W)) -> regFile(11),
    (io.r_rs2 === 12.U(5.W)) -> regFile(12),
    (io.r_rs2 === 13.U(5.W)) -> regFile(13),
    (io.r_rs2 === 14.U(5.W)) -> regFile(14),
    (io.r_rs2 === 15.U(5.W)) -> regFile(15),
    (io.r_rs2 === 16.U(5.W)) -> regFile(16),
    (io.r_rs2 === 17.U(5.W)) -> regFile(17),
    (io.r_rs2 === 18.U(5.W)) -> regFile(18),
    (io.r_rs2 === 19.U(5.W)) -> regFile(19),
    (io.r_rs2 === 20.U(5.W)) -> regFile(20),
    (io.r_rs2 === 21.U(5.W)) -> regFile(21),
    (io.r_rs2 === 22.U(5.W)) -> regFile(22),
    (io.r_rs2 === 23.U(5.W)) -> regFile(23),
    (io.r_rs2 === 24.U(5.W)) -> regFile(24),
    (io.r_rs2 === 25.U(5.W)) -> regFile(25),
    (io.r_rs2 === 26.U(5.W)) -> regFile(26),
    (io.r_rs2 === 27.U(5.W)) -> regFile(27),
    (io.r_rs2 === 28.U(5.W)) -> regFile(28),
    (io.r_rs2 === 29.U(5.W)) -> regFile(29),
    (io.r_rs2 === 30.U(5.W)) -> regFile(30),
    (io.r_rs2 === 31.U(5.W)) -> regFile(31)
    ))
}

package register

import chisel3._

class Regs extends Module {
  val io = IO(new Bundle {
    val data = Input(UInt(64.W))
    val rd = Input(UInt(5.W))
    val regOut = Output(Vec(32, UInt(64.W)))
  })

  val regFile = RegInit(VecInit(Seq.fill(32)(0.U(64.W))))

  for (i <- 0 to 31) {
    io.regOut(i) := regFile(i)
  }

  regFile(0) := 0.U(64.W)
  
  for (i <- 1 to 31) {
    regFile(i) := Mux(io.rd === i.asUInt, io.data, regFile(i))
  }
}

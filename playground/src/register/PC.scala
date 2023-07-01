package register

import chisel3._

class PC extends Module {
  val io = IO(new Bundle {
    val next_pc = Input(UInt(64.W))
    val pc = Output(UInt(64.W))
  })

  val pc_reg = RegNext(io.next_pc, "h80000000".U(64.W))

  val pc2cpp = Module(new PC2cpp)

  io.pc := pc_reg
  pc2cpp.io.pc_val := pc_reg
}

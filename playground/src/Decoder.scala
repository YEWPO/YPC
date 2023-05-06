import chisel3._
import chisel3.util._

class Decoder extends Module {
  val io = IO(new Bundle {
    val inst = Input(UInt(32.W))
    val rs1 = Output(UInt(5.W))
    val rd = Output(UInt(5.W))
    val imm = Output(UInt(64.W))
    val ebreak = Output(Bool())
  })

  io.rs1 := io.inst(19, 15)
  io.rd := io.inst(11, 7)

  io.imm := Cat(Fill(52, io.inst(31)), io.inst(31, 20))

  io.ebreak := Mux(io.inst === "h00100073".U, true.B, false.B)
}

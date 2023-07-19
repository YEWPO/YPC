import chisel3._
import chisel3.util._

class Top extends Module {
  val io = IO(new Bundle {
    val inst = Input(UInt(32.W))
    val pc = Output(UInt(64.W))
  })

  io.pc := Mux(io.inst.xorR === true.B, "h0000_0000_8000_0000".U, "h0000_0000_8000_0004".U)
}

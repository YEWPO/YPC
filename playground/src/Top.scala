import  chisel3._
import register._

class Top extends Module {
  val io = IO(new Bundle{
    val inst = Input(UInt(32.W))
    val out = Output(UInt(64.W))
    val pc = Output(UInt(64.W))
  })

  val pc = Module(new PC)

  io.out := io.inst
  io.pc := pc.io.pc
  
  pc.io.next_pc := pc.io.pc + 4.U
}

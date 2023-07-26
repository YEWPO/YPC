import chisel3._
import chisel3.util._
import utils._

class Top extends Module {
  val io = IO(new Bundle {
    val pc = Output(UInt(64.W))
  })
}

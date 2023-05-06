import chisel3._

class Adder extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(64.W))
    val b = Input(UInt(64.W))
    val out = Output(UInt(64.W))
  })

  io.out := io.a + io.b
}

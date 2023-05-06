import  chisel3._

class Top extends Module {
  val io = IO(new Bundle {
    val inst = Input(UInt(32.W))
    val regOut = Output(Vec(32, UInt(64.W)))
    val pc = Output(UInt(64.W))
  })

  val pcReg = RegInit("h80000000".U)

  val regs = Module(new Regs)
  val mux = Module(new Mux)
  val adder = Module(new Adder)
  val decoder = Module(new Decoder)
  val ebreak = Module(new Ebreak)

  regs.io.data := adder.io.out
  regs.io.rd := decoder.io.rd
  
  ebreak.io.enable := decoder.io.ebreak

  mux.io.regInput := regs.io.regOut
  mux.io.idx := decoder.io.rs1

  adder.io.a := mux.io.out
  adder.io.b := decoder.io.imm

  decoder.io.inst := io.inst
  
  io.regOut := regs.io.regOut

  pcReg := pcReg + 4.U

  io.pc := pcReg
}

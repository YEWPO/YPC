package stages

import chisel3._
import bundles._
import macros._
import utils.instfetch._

class IF extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new Bundle {
    val in = Input(new Bundle {
      val data = new PreIFDataBundle
    })
    val out = Output(new Bundle {
      val data = new IF2IDDataBundle
    })
  })

  /* ========== Module ========== */
  val inst_mem = Module(new InstMem)

  /* ========== Combinational Circuit ========== */
  inst_mem.io.addr := io.in.data.pc

  io.out.data.inst := Mux(
    io.in.data.pc(2).orR,
    CommonMacros.getWord(inst_mem.io.r_data, 1),
    CommonMacros.getWord(inst_mem.io.r_data, 0)
  )
  io.out.data.pc   := io.in.data.pc
  io.out.data.snpc := io.in.data.pc + 4.U
}

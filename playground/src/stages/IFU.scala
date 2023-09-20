package stages

import chisel3._
import bundles._
import macros._
import utils.instfetch._

class IFUIO extends Bundle {
  val in = Input(new Bundle {
    val dnpc      = UInt(64.W)
    val expt_pc   = UInt(64.W)
    val expt_op   = Bool()
    val jump_ctl  = Bool()
    val pc_enable = Bool()
  })
  val out = Output(new Bundle {
    val data = new IF2IDDataBundle
  })
}

class IFU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new IFUIO)

  /* ========== Module ========== */
  // val inst_ram = Module(new InstRAM)
  val inst_mem = Module(new InstMem)

  /* ========== Register ========== */
  val pc = RegInit(CommonMacros.PC_RESET_VAL)

  /* ========== Wire ========== */
  val snpc = pc + 4.U
  val npc = Mux(
    io.in.expt_op,
    io.in.expt_pc,
    // Mux(
    //   inst_ram.io.r.valid,
    Mux(io.in.jump_ctl, io.in.dnpc, snpc)
    //   pc
    // )
  )

  /* ========== Sequential Circuit ========== */
  when(io.in.pc_enable) {
    pc := npc
  }

  /* ========== Combinational Circuit ========== */
  /* inst_ram.io.ar.bits.addr := pc
  inst_ram.io.ar.bits.prot := 0.U
  inst_ram.io.ar.valid     := true.B

  inst_ram.io.r.ready := true.B */

  inst_mem.io.addr := pc

  io.out.data.inst := Mux(
    pc(2).orR,
    // CommonMacros.getWord(inst_ram.io.r.bits.data, 1),
    // CommonMacros.getWord(inst_ram.io.r.bits.data, 0)
    CommonMacros.getWord(inst_mem.io.r_data, 1),
    CommonMacros.getWord(inst_mem.io.r_data, 0)
  )
  io.out.data.pc   := pc
  io.out.data.snpc := snpc
}

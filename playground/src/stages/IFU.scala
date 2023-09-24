package stages

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
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
  val if2id = Decoupled(new IF2IDBundle)
}

class IFU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new IFUIO)

  /* ========== Module ========== */
  val inst_mem = Module(new InstMem)

  /* ========== Register ========== */
  val pc = RegInit(CommonMacros.PC_RESET_VAL)

  val r_valid = RegInit(false.B)
  val r_if2id = RegInit(IF2IDBundle.if2id_rst_val)

  /* ========== Wire ========== */
  val valid_enable = !io.if2id.valid || io.if2id.ready
  val valid_next   = r_valid && !io.if2id.fire

  val snpc = pc + 4.U
  val npc = Mux(
    io.in.expt_op,
    io.in.expt_pc,
    Mux(io.in.jump_ctl, io.in.dnpc, snpc)
  )
  val inst = Mux(
    pc(2).orR,
    CommonMacros.getWord(inst_mem.io.r_data, 1),
    CommonMacros.getWord(inst_mem.io.r_data, 0)
  )

  /* ========== Sequential Circuit ========== */
  r_valid := Mux(valid_enable, true.B, valid_next)

  r_if2id.data.pc   := pc
  r_if2id.data.snpc := snpc
  r_if2id.data.inst := inst

  pc := Mux(io.if2id.fire, npc, pc)

  /* ========== Combinational Circuit ========== */
  io.if2id.valid := r_valid

  inst_mem.io.addr := pc

  io.if2id.bits.data := r_if2id.data
}

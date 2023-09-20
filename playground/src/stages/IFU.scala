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

  /* ========== Parameter ========== */
  val s_idle :: s_wait_ready :: Nil = Enum(2)

  /* ========== Module ========== */
  val inst_mem = Module(new InstMem)
  val if2id_rst_val = (new IF2IDBundle).Lit(
    _.data -> (new IF2IDDataBundle).Lit(
      _.pc   -> CommonMacros.PC_RESET_VAL,
      _.snpc -> CommonMacros.PC_RESET_VAL,
      _.inst -> CommonMacros.INST_RESET_VAL
    )
  )

  /* ========== Register ========== */
  val pc      = RegInit(CommonMacros.PC_RESET_VAL)
  val r_state = RegInit(s_idle)
  val r_if2id = RegInit(if2id_rst_val)
  val r_valid = RegInit(false.B)

  /* ========== Wire ========== */
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
  when(r_state === s_idle) {
    r_state := Mux(io.if2id.valid, s_wait_ready, s_idle)

    r_valid := true.B

    r_if2id.data.pc   := pc
    r_if2id.data.snpc := snpc
    r_if2id.data.inst := inst
  }.elsewhen(r_state === s_wait_ready) {
    r_state := Mux(io.if2id.fire, s_idle, s_wait_ready)

    r_valid := Mux(io.if2id.fire, false.B, r_valid)

    r_if2id := Mux(io.if2id.fire, if2id_rst_val, r_if2id)

    pc := Mux(io.if2id.fire, npc, pc)
  }

  /* ========== Combinational Circuit ========== */
  inst_mem.io.addr := pc

  io.if2id.bits.data := r_if2id.data
}

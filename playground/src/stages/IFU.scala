package stages

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import bundles._
import macros._
import utils.instfetch._

class IFUIO extends Bundle {
  val in = Input(new Bundle {
    val dnpc     = UInt(64.W)
    val tvec     = UInt(64.W)
    val cause    = UInt(64.W)
    val jump_ctl = Bool()
  })
  val if2id = Decoupled(new IF2IDBundle)
}

class IFU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new IFUIO)

  /* ========== Module ========== */
  val inst_ram = Module(new InstRAM)

  /* ========== Register ========== */
  val pc = RegInit(CommonMacros.PC_RESET_VAL)

  val r_valid = RegInit(false.B)
  val r_if2id = RegInit(IF2IDBundle.if2id_rst_val)

  val r_ar_valid = RegInit(false.B)
  val r_ar_addr  = RegInit(CommonMacros.PC_RESET_VAL)

  val r_dnpc        = RegInit(CommonMacros.PC_RESET_VAL)
  val r_dnpc_valid  = RegInit(false.B)
  val r_cause       = RegInit(CommonMacros.CAUSE_RESET_VAL)
  val r_cause_valid = RegInit(false.B)

  /* ========== Wire ========== */
  val dnpc        = Mux(r_dnpc_valid, r_dnpc, io.in.dnpc)
  val dnpc_valid  = r_dnpc_valid || io.in.jump_ctl
  val cause       = Mux(r_cause_valid, r_cause, io.in.cause)
  val cause_valid = r_cause_valid || (io.in.cause =/= CommonMacros.CAUSE_RESET_VAL)

  val valid_enable  = (!io.if2id.valid || io.if2id.ready) && inst_ram.io.r.fire
  val valid_next    = r_valid && !io.if2id.fire
  val valid_current = !dnpc_valid && !cause_valid

  val inst_req      = (!r_ar_valid && !inst_ram.io.r.valid) || inst_ram.io.r.fire
  val ar_valid_next = r_ar_valid && !inst_ram.io.ar.ready

  val snpc = pc + 4.U
  val npc = Mux(
    cause_valid,
    io.in.tvec,
    Mux(dnpc_valid, dnpc, snpc)
  )
  val inst = Mux(
    pc(2).orR,
    CommonMacros.getWord(inst_ram.io.r.bits.data, 1),
    CommonMacros.getWord(inst_ram.io.r.bits.data, 0)
  )

  /* ========== Sequential Circuit ========== */
  r_dnpc_valid  := Mux(inst_req, false.B, io.in.jump_ctl)
  r_dnpc        := Mux(r_dnpc_valid, r_dnpc, io.in.dnpc)
  r_cause_valid := Mux(inst_req, false.B, io.in.cause =/= CommonMacros.CAUSE_RESET_VAL)
  r_cause       := Mux(r_cause_valid, r_cause, io.in.cause)

  r_valid := Mux(valid_enable, valid_current, valid_next)

  r_if2id.data.pc    := Mux(valid_enable, pc, r_if2id.data.pc)
  r_if2id.data.snpc  := Mux(valid_enable, snpc, r_if2id.data.snpc)
  r_if2id.data.inst  := Mux(valid_enable, inst, r_if2id.data.inst)
  r_if2id.data.cause := CommonMacros.CAUSE_RESET_VAL

  pc := Mux(inst_req, npc, pc)

  r_ar_valid := Mux(inst_req, true.B, ar_valid_next)
  r_ar_addr  := Mux(inst_req, npc, r_ar_addr)

  /* ========== Combinational Circuit ========== */
  inst_ram.io.ar.bits.addr := r_ar_addr
  inst_ram.io.ar.bits.prot := 0.U(3.W)
  inst_ram.io.ar.valid     := r_ar_valid

  inst_ram.io.r.ready := inst_ram.io.r.valid && (!io.if2id.valid || io.if2id.ready)

  io.if2id.valid := r_valid

  io.if2id.bits.data := r_if2id.data
}

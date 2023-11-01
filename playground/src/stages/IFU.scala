package stages

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import bundles._
import macros._

class IFUIO extends Bundle {
  val in = Input(new Bundle {
    val dnpc     = UInt(64.W)
    val tvec     = UInt(64.W)
    val cause    = UInt(64.W)
    val jump_ctl = Bool()
  })
  val if2id = Decoupled(new IF2IDBundle)

  val ar = Decoupled(new AXILiteReadAddrBundle)
  val r  = Flipped(Decoupled(new AXILiteReadDataBundle))
}

class IFU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new IFUIO)

  /* ========== Register ========== */
  val r_idle :: r_wait_ready :: r_wait_data :: Nil = Enum(3)

  /* ========== Register ========== */
  val pc = RegInit(CommonMacros.PC_RESET_VAL)

  val r_valid = RegInit(false.B)
  val r_if2id = RegInit(IF2IDBundle.if2id_rst_val)

  val r_dnpc        = RegInit(CommonMacros.PC_RESET_VAL)
  val r_dnpc_valid  = RegInit(false.B)
  val r_cause_valid = RegInit(false.B)

  // axi read
  val r_state = RegInit(r_idle)

  /* ========== Wire ========== */
  val dnpc        = Mux(r_dnpc_valid, r_dnpc, io.in.dnpc)
  val dnpc_valid  = r_dnpc_valid || io.in.jump_ctl
  val cause_valid = r_cause_valid || (io.in.cause =/= CommonMacros.CAUSE_RESET_VAL)

  val valid_enable  = (!io.if2id.valid || io.if2id.ready) && io.r.fire
  val valid_next    = r_valid && !io.if2id.fire
  val valid_current = !dnpc_valid && !cause_valid

  val update_state = ((r_state === r_wait_data) && io.r.valid) || (r_state === r_idle)

  val snpc = pc + 4.U
  val npc = Mux(
    cause_valid,
    io.in.tvec,
    Mux(dnpc_valid, dnpc, snpc)
  )
  val inst = Mux(
    pc(2).orR,
    CommonMacros.getWord(io.r.bits.data, 1),
    CommonMacros.getWord(io.r.bits.data, 0)
  )

  /* ========== Sequential Circuit ========== */
  r_dnpc_valid  := !update_state && Mux(!r_dnpc_valid, io.in.jump_ctl, r_dnpc_valid)
  r_dnpc        := Mux(r_dnpc_valid, r_dnpc, io.in.dnpc)
  r_cause_valid := !update_state && Mux(!r_cause_valid, io.in.cause =/= CommonMacros.CAUSE_RESET_VAL, r_cause_valid)

  r_valid := Mux(valid_enable, valid_current, valid_next)

  r_if2id.data.pc    := Mux(valid_enable, pc, r_if2id.data.pc)
  r_if2id.data.snpc  := Mux(valid_enable, snpc, r_if2id.data.snpc)
  r_if2id.data.inst  := Mux(valid_enable, inst, r_if2id.data.inst)
  r_if2id.data.cause := CommonMacros.CAUSE_RESET_VAL

  r_state := MuxLookup(r_state, r_idle)(
    Seq(
      r_idle       -> r_wait_ready,
      r_wait_ready -> Mux(io.ar.ready, r_wait_data, r_wait_ready),
      r_wait_data  -> Mux(io.r.valid, r_wait_ready, r_wait_data)
    )
  )

  pc := Mux(update_state, Mux(io.r.valid && io.if2id.valid && !io.if2id.ready, pc, npc), pc)

  /* ========== Combinational Circuit ========== */
  io.ar.bits.addr := Mux(update_state, Mux(io.r.valid && io.if2id.valid && !io.if2id.ready, pc, npc), pc)
  io.ar.bits.prot := 0.U(3.W)
  io.ar.valid     := Mux(update_state || (r_state === r_wait_ready), true.B, false.B)

  io.r.ready := io.r.valid

  io.if2id.valid := r_valid

  io.if2id.bits.data := r_if2id.data
}

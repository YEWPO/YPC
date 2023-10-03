package utils.instfetch

import chisel3._
import chisel3.util._
import bundles._

class InstRAM extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new Bundle {
    val ar = Flipped(Decoupled(new AXILiteReadAddrBundle))
    val r  = Decoupled(new AXILiteReadDataBundle)
  })

  /* ========== Module ========== */
  val inst_mem = Module(new InstMem)

  /* ========== Register ========== */
  val r_rdata  = RegInit(0.U(64.W))
  val r_rvalid = RegInit(false.B)

  /* ========== Wire ========== */
  val rvalid_enable = io.ar.valid && (!io.r.valid || io.r.ready)
  val rvalid_next   = r_rvalid && !io.r.ready

  /* ========== Sequential Circuit ========== */
  r_rvalid := Mux(rvalid_enable, true.B, rvalid_next)
  r_rdata  := inst_mem.io.r_data

  /* ========== Combinational Circuit ========== */
  inst_mem.io.addr := io.ar.bits.addr

  io.ar.ready    := io.ar.valid && (!io.r.valid || io.r.ready)
  io.r.bits.resp := 0.U(2.W)
  io.r.valid     := r_rvalid
  io.r.bits.data := r_rdata
}

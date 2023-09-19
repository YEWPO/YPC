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
  val r_arready = RegInit(false.B)
  val r_rdata   = RegInit(0.U(64.W))
  val r_rvalid  = RegInit(false.B)

  /* ========== Wire ========== */
  val arready_enable = io.ar.valid && !io.ar.ready && (!io.r.valid || io.r.ready)
  val rvalid_enable  = arready_enable
  val rvalid_next    = r_rvalid && !io.r.ready

  /* ========== Sequential Circuit ========== */
  r_arready := Mux(arready_enable, true.B, false.B)
  r_rvalid  := Mux(rvalid_enable, true.B, rvalid_next)
  r_rdata   := inst_mem.io.r_data

  /* ========== Combinational Circuit ========== */
  inst_mem.io.addr := io.ar.bits.addr

  io.ar.ready    := r_arready
  io.r.bits.resp := 0.U(2.W)
  io.r.valid     := r_rvalid
  io.r.bits.data := r_rdata
}

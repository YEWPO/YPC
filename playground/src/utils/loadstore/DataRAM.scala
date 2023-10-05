package utils.loadstore

import chisel3._
import bundles._
import chisel3.util._

class DataRAMIO extends Bundle {
  val ar = Flipped(Decoupled(new AXILiteReadAddrBundle))
  val r  = Decoupled(new AXILiteReadDataBundle)
  val aw = Flipped(Decoupled(new AXILiteWriteAddrBundle))
  val w  = Flipped(Decoupled(new AXILiteWriteDataBundle))
  val b  = Decoupled(new AXILiteWriteRespBundle)
}

class DataRAM extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new DataRAMIO)

  /* ========== Module ========== */
  val mem_read  = Module(new MemRead)
  val mem_write = Module(new MemWrite)

  /* ========== Register ========== */
  val r_rvalid = RegInit(false.B)
  val r_rdata  = RegInit(0.U(64.W))

  val r_bvalid = RegInit(false.B)

  /* ========== Wire ========== */
  val rvalid_enable = io.ar.valid && (!io.r.valid || io.r.ready)
  val rvalid_next   = r_rvalid && !io.r.ready

  val bvalid_enable = io.aw.valid && io.w.valid && (!io.b.valid || io.b.ready)
  val bvalid_next   = r_bvalid && !io.b.ready

  /* ========== Sequential Cicuit ========== */
  r_rvalid := Mux(rvalid_enable, true.B, rvalid_next)
  r_rdata  := mem_read.io.r_data

  r_bvalid := Mux(bvalid_enable, true.B, bvalid_next)

  /* ========== Combinational Cicuit ========== */
  mem_read.io.addr := io.ar.bits.addr

  mem_write.io.addr   := io.aw.bits.addr
  mem_write.io.mask   := io.w.bits.strb
  mem_write.io.w_data := io.w.bits.data

  io.ar.ready := io.ar.valid && (!io.r.valid || io.r.ready)

  io.r.valid     := r_rvalid
  io.r.bits.data := r_rdata
  io.r.bits.resp := 0.U(2.W)

  io.aw.ready := io.aw.valid && io.w.valid && (!io.b.valid || io.b.ready)

  io.w.ready := io.aw.valid && io.w.valid && (!io.b.valid || io.b.ready)

  io.b.valid     := r_bvalid
  io.b.bits.resp := 0.U(2.W)
}

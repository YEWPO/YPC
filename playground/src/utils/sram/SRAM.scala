package utils.sram

import chisel3._
import bundles._
import chisel3.util._

class SRAMIO extends Bundle {
  val ar = Flipped(Decoupled(new AXILiteReadAddrBundle))
  val r  = Decoupled(new AXILiteReadDataBundle)
  val aw = Flipped(Decoupled(new AXILiteWriteAddrBundle))
  val w  = Flipped(Decoupled(new AXILiteWriteDataBundle))
  val b  = Decoupled(new AXILiteWriteRespBundle)
}

class SRAM extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new SRAMIO)

  /* ========== Module ========== */
  val mem_read  = Module(new MemRead)
  val mem_write = Module(new MemWrite)

  /* ========== Register ========== */
  val r_araddr = RegInit(0.U(64.W))
  val r_arready = RegInit(false.B)

  val r_rvalid = RegInit(false.B)
  val r_rdata  = RegInit(0.U(64.W))

  val r_bvalid = RegInit(false.B)

  /* ========== Wire ========== */
  val r_en = io.ar.valid && r_arready && !r_rvalid

  val rvalid_next   = r_rvalid && !io.r.ready
  val rdata_next    = Mux(io.r.fire, 0.U(64.W), r_rdata)

  val bvalid_enable = io.aw.valid && io.w.valid && (!io.b.valid || io.b.ready)
  val bvalid_next   = r_bvalid && !io.b.ready

  /* ========== Sequential Cicuit ========== */
  r_araddr := Mux(io.ar.valid && !r_arready, io.ar.bits.addr, r_araddr)
  r_arready := Mux(io.ar.valid && !r_arready, true.B, false.B);

  r_rvalid := Mux(r_en, true.B, rvalid_next)
  r_rdata  := Mux(r_en, mem_read.io.r_data, rdata_next)

  r_bvalid := Mux(bvalid_enable, true.B, bvalid_next)

  /* ========== Combinational Cicuit ========== */
  mem_read.io.addr := r_araddr

  mem_write.io.addr   := io.aw.bits.addr
  mem_write.io.mask   := io.w.bits.strb
  mem_write.io.w_data := io.w.bits.data

  io.ar.ready := r_arready

  io.r.valid     := r_rvalid
  io.r.bits.data := r_rdata
  io.r.bits.resp := 0.U(2.W)

  io.aw.ready := io.aw.valid && io.w.valid && (!io.b.valid || io.b.ready)

  io.w.ready := io.aw.valid && io.w.valid && (!io.b.valid || io.b.ready)

  io.b.valid     := r_bvalid
  io.b.bits.resp := 0.U(2.W)
}

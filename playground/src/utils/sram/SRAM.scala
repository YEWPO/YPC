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

  // ========== Parameter ==========
  val r_wait_addr :: r_prepare_data :: r_wait_rready :: Nil = Enum(3)

  /* ========== Register ========== */
  val r_state = RegInit(r_wait_addr)

  val r_araddr  = RegInit(0.U(64.W))
  val r_arready = RegInit(false.B)
  val r_rdata   = RegInit(0.U(64.W))

  val r_awaddr  = RegInit(0.U(64.W))
  val r_awready = RegInit(false.B)

  val r_wdata  = RegInit(0.U(64.W))
  val r_wready = RegInit(false.B)

  val r_bvalid = RegInit(false.B)

  /* ========== Wire ========== */
  val w_en = io.aw.valid && r_awready && io.w.valid && r_wready && !r_bvalid

  val update_addr =
    ((r_state === r_wait_addr) && io.ar.valid) || ((r_state === r_wait_rready) && io.r.ready && io.ar.valid)

  val bvalid_next = r_bvalid && !io.b.ready

  /* ========== Sequential Cicuit ========== */
  r_state := MuxLookup(r_state, r_wait_addr)(
    Seq(
      r_wait_addr    -> Mux(io.ar.valid, r_prepare_data, r_wait_addr),
      r_prepare_data -> r_wait_rready,
      r_wait_rready  -> Mux(io.r.ready && io.ar.valid, r_prepare_data, Mux(io.r.ready, r_wait_addr, r_wait_rready))
    )
  )

  r_araddr  := Mux(update_addr, io.ar.bits.addr, r_araddr)
  r_arready := update_addr
  r_rdata   := Mux(r_state === r_prepare_data, mem_read.io.r_data, r_rdata)

  r_awaddr  := Mux(io.aw.valid && !r_awready && io.w.valid, io.aw.bits.addr, r_awaddr)
  r_awready := io.aw.valid && !r_awready && io.w.valid

  r_wdata  := Mux(io.w.valid && !r_wready && io.aw.valid, io.w.bits.data, r_wdata)
  r_wready := io.w.valid && !r_wready && io.aw.valid

  r_bvalid := Mux(w_en, true.B, bvalid_next)

  /* ========== Combinational Cicuit ========== */
  mem_read.io.addr := r_araddr

  mem_write.io.addr   := r_awaddr
  mem_write.io.mask   := Mux(w_en, io.w.bits.strb, 0.U(8.W))
  mem_write.io.w_data := r_wdata

  io.ar.ready := r_arready

  io.r.valid     := r_state === r_wait_rready
  io.r.bits.data := r_rdata
  io.r.bits.resp := 0.U(2.W)

  io.aw.ready := r_awready

  io.w.ready := r_wready

  io.b.valid     := r_bvalid
  io.b.bits.resp := 0.U(2.W)
}

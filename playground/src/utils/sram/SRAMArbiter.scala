package utils.sram

import chisel3._
import chisel3.util._
import bundles._

class SRAMArbiterIO extends Bundle {
  val ifu = new Bundle {
    val ar = Flipped(Decoupled(new AXILiteReadAddrBundle))
    val r  = Decoupled(new AXILiteReadDataBundle)
  }

  val lsu = new Bundle {
    val ar = Flipped(Decoupled(new AXILiteReadAddrBundle))
    val r  = Decoupled(new AXILiteReadDataBundle)
    val aw = Flipped(Decoupled(new AXILiteWriteAddrBundle))
    val w  = Flipped(Decoupled(new AXILiteWriteDataBundle))
    val b  = Decoupled(new AXILiteWriteRespBundle)
  }

  val out = new Bundle {
    val ar = Decoupled(new AXILiteReadAddrBundle)
    val r  = Flipped(Decoupled(new AXILiteReadDataBundle))
    val aw = Decoupled(new AXILiteWriteAddrBundle)
    val w  = Decoupled(new AXILiteWriteDataBundle)
    val b  = Flipped(Decoupled(new AXILiteWriteRespBundle))
  }
}

class SRAMArbiter extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new SRAMArbiterIO)

  /* ========== Parameter ========== */
  val c_idle :: c_ifu :: c_lsu :: Nil = Enum(3)

  /* ========== Register ========== */
  val r_client = RegInit(c_idle)

  /* ========== Wire ========== */
  val client_req    = Cat(io.lsu.ar.valid, io.ifu.ar.valid)
  val client_enable = !io.out.r.valid || io.out.r.fire
  val client_selector = Seq(
    "b11".U -> c_lsu,
    "b10".U -> c_lsu,
    "b01".U -> c_ifu,
    "b00".U -> c_idle
  )

  /* ========== Sequential Circuit ========== */
  r_client := Mux(client_enable, MuxLookup(client_req, c_idle)(client_selector), r_client)

  /* ========== Combinational Circuit ========== */
  io.out.ar.valid := client_req.orR
  io.out.ar.bits.addr := Mux(
    client_req(1),
    io.lsu.ar.bits.addr,
    Mux(
      client_req(0),
      io.ifu.ar.bits.addr,
      0.U(64.W)
    )
  )
  io.out.ar.bits.prot := Mux(
    client_req(1),
    io.lsu.ar.bits.prot,
    Mux(
      client_req(0),
      io.ifu.ar.bits.prot,
      0.U(3.W)
    )
  )

  io.ifu.ar.ready := Mux(r_client === c_ifu, io.out.ar.ready, false.B)
  io.lsu.ar.ready := Mux(r_client === c_lsu, io.out.ar.ready, false.B)

  io.ifu.r.valid     := Mux(r_client === c_ifu, io.out.r.valid, false.B)
  io.ifu.r.bits.data := Mux(r_client === c_ifu, io.out.r.bits.data, 0.U(64.W))
  io.ifu.r.bits.resp := Mux(r_client === c_ifu, io.out.r.bits.resp, 0.U(2.W))
  io.lsu.r.valid     := Mux(r_client === c_lsu, io.out.r.valid, false.B)
  io.lsu.r.bits.data := Mux(r_client === c_lsu, io.out.r.bits.data, 0.U(64.W))
  io.lsu.r.bits.resp := Mux(r_client === c_lsu, io.out.r.bits.resp, 0.U(2.W))

  io.out.r.ready := MuxLookup(r_client, false.B)(
    Seq(
      c_idle -> false.B,
      c_ifu  -> io.ifu.r.ready,
      c_lsu  -> io.lsu.r.ready
    )
  )

  io.out.aw <> io.lsu.aw
  io.out.w  <> io.lsu.w
  io.out.b  <> io.lsu.b
}

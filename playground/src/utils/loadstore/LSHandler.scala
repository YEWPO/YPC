package utils.loadstore

import chisel3._
import chisel3.util._
import macros._
import bundles._

class LSHandlerIO extends Bundle {
  val addr    = Input(UInt(64.W))
  val w_data  = Input(UInt(64.W))
  val mem_ctl = Input(UInt(5.W))

  val r_data = Output(UInt(64.W))

  val fin = Output(Bool())

  val ar = Decoupled(new AXILiteReadAddrBundle)
  val r  = Flipped(Decoupled(new AXILiteReadDataBundle))
  val aw = Decoupled(new AXILiteWriteAddrBundle)
  val w  = Decoupled(new AXILiteWriteDataBundle)
  val b  = Flipped(Decoupled(new AXILiteWriteRespBundle))
}

class LSHandler extends Module {
  val io = IO(new LSHandlerIO)

  val r_en = io.mem_ctl(3).orR
  val w_en = io.mem_ctl(4).orR

  // generate mask
  val mask_0 = (io.mem_ctl === ControlMacros.MEM_CTL_SB && io.addr(2, 0) === 0.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SH && io.addr(2, 1) === 0.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SW && io.addr(2) === 0.U) ||
    io.mem_ctl === ControlMacros.MEM_CTL_SD
  val mask_1 = (io.mem_ctl === ControlMacros.MEM_CTL_SB && io.addr(2, 0) === 1.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SH && io.addr(2, 1) === 0.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SW && io.addr(2) === 0.U) ||
    io.mem_ctl === ControlMacros.MEM_CTL_SD
  val mask_2 = (io.mem_ctl === ControlMacros.MEM_CTL_SB && io.addr(2, 0) === 2.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SH && io.addr(2, 1) === 1.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SW && io.addr(2) === 0.U) ||
    io.mem_ctl === ControlMacros.MEM_CTL_SD
  val mask_3 = (io.mem_ctl === ControlMacros.MEM_CTL_SB && io.addr(2, 0) === 3.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SH && io.addr(2, 1) === 1.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SW && io.addr(2) === 0.U) ||
    io.mem_ctl === ControlMacros.MEM_CTL_SD
  val mask_4 = (io.mem_ctl === ControlMacros.MEM_CTL_SB && io.addr(2, 0) === 4.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SH && io.addr(2, 1) === 2.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SW && io.addr(2) === 1.U) ||
    io.mem_ctl === ControlMacros.MEM_CTL_SD
  val mask_5 = (io.mem_ctl === ControlMacros.MEM_CTL_SB && io.addr(2, 0) === 5.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SH && io.addr(2, 1) === 2.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SW && io.addr(2) === 1.U) ||
    io.mem_ctl === ControlMacros.MEM_CTL_SD
  val mask_6 = (io.mem_ctl === ControlMacros.MEM_CTL_SB && io.addr(2, 0) === 6.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SH && io.addr(2, 1) === 3.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SW && io.addr(2) === 1.U) ||
    io.mem_ctl === ControlMacros.MEM_CTL_SD
  val mask_7 = (io.mem_ctl === ControlMacros.MEM_CTL_SB && io.addr(2, 0) === 7.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SH && io.addr(2, 1) === 3.U) ||
    (io.mem_ctl === ControlMacros.MEM_CTL_SW && io.addr(2) === 1.U) ||
    io.mem_ctl === ControlMacros.MEM_CTL_SD
  val mask = Cat(mask_7, mask_6, mask_5, mask_4, mask_3, mask_2, mask_1, mask_0)

  // generate data to memory
  val sb_data = Fill(8, CommonMacros.getByte(io.w_data, 0))
  val sh_data = Fill(4, CommonMacros.getHalfWord(io.w_data, 0))
  val sw_data = Fill(2, CommonMacros.getWord(io.w_data, 0))
  val sd_data = io.w_data

  val w_data_map = Seq(
    ControlMacros.MEM_CTL_SB -> sb_data,
    ControlMacros.MEM_CTL_SH -> sh_data,
    ControlMacros.MEM_CTL_SW -> sw_data,
    ControlMacros.MEM_CTL_SD -> sd_data
  )
  val w_data = MuxLookup(io.mem_ctl, io.w_data)(w_data_map)

  /* ========== Register ========== */
  val r_arvalid = RegInit(false.B)
  val r_araddr  = RegInit(0.U(64.W))

  val r_awvalid = RegInit(false.B)
  val r_awaddr  = RegInit(0.U(64.W))
  val r_wvalid  = RegInit(false.B)
  val r_wdata   = RegInit(0.U(64.W))
  val r_wstrb   = RegInit(0.U(8.W))

  /* ========== Wire ========== */
  val arvalid_next = r_arvalid && !io.ar.fire
  val araddr_next  = Mux(io.ar.fire, 0.U(64.W), r_araddr)
  val awvalid_next = r_awvalid && !io.aw.fire
  val awaddr_next  = Mux(io.aw.fire, 0.U(64.W), r_awaddr)
  val wvalid_next  = r_wvalid && !io.w.fire
  val wdata_next   = Mux(io.w.fire, 0.U(64.W), r_wdata)
  val wstrb_next   = Mux(io.w.fire, 0.U(8.W), r_wstrb)

  // write to memory
  val w_req = w_en && (!r_awvalid && !r_wvalid && !io.b.valid)

  r_awvalid := Mux(w_req, true.B, awvalid_next)
  r_awaddr  := Mux(w_req, io.addr, awaddr_next)
  r_wvalid  := Mux(w_req, true.B, wvalid_next)
  r_wdata   := Mux(w_req, w_data, wdata_next)
  r_wstrb   := Mux(w_req, mask, wstrb_next)

  io.aw.valid     := r_awvalid
  io.aw.bits.addr := r_awaddr
  io.aw.bits.prot := 0.U(3.W)
  io.w.valid      := r_wvalid
  io.w.bits.data  := r_wdata
  io.w.bits.strb  := r_wstrb

  io.b.ready := io.b.valid

  // read from memory
  val r_req = r_en && (!r_arvalid && !io.r.valid)

  r_arvalid := Mux(r_req, true.B, arvalid_next)
  r_araddr  := Mux(r_req, io.addr, araddr_next)

  io.ar.valid     := r_arvalid
  io.ar.bits.addr := r_araddr
  io.ar.bits.prot := 0.U(3.W)

  io.r.ready := io.r.valid

  val r_data = io.r.bits.data

  // handle read data
  val lb_data_map = Seq(
    0.U -> CommonMacros.getByte(r_data, 0),
    1.U -> CommonMacros.getByte(r_data, 1),
    2.U -> CommonMacros.getByte(r_data, 2),
    3.U -> CommonMacros.getByte(r_data, 3),
    4.U -> CommonMacros.getByte(r_data, 4),
    5.U -> CommonMacros.getByte(r_data, 5),
    6.U -> CommonMacros.getByte(r_data, 6),
    7.U -> CommonMacros.getByte(r_data, 7)
  )
  val lh_data_map = Seq(
    0.U -> CommonMacros.getHalfWord(r_data, 0),
    1.U -> CommonMacros.getHalfWord(r_data, 1),
    2.U -> CommonMacros.getHalfWord(r_data, 2),
    3.U -> CommonMacros.getHalfWord(r_data, 3)
  )
  val lw_data_map = Seq(
    0.U -> CommonMacros.getWord(r_data, 0),
    1.U -> CommonMacros.getWord(r_data, 1)
  )
  val lb_data = MuxLookup(io.addr(2, 0), 0.U(8.W))(lb_data_map)
  val lh_data = MuxLookup(io.addr(2, 1), 0.U(16.W))(lh_data_map)
  val lw_data = MuxLookup(io.addr(2), 0.U(32.W))(lw_data_map)
  val ld_data = r_data

  // return read data
  val r_data_map = Seq(
    ControlMacros.MEM_CTL_LB  -> CommonMacros.signExtend(lb_data),
    ControlMacros.MEM_CTL_LH  -> CommonMacros.signExtend(lh_data),
    ControlMacros.MEM_CTL_LW  -> CommonMacros.signExtend(lw_data),
    ControlMacros.MEM_CTL_LD  -> ld_data,
    ControlMacros.MEM_CTL_LBU -> CommonMacros.zeroExtend(lb_data),
    ControlMacros.MEM_CTL_LHU -> CommonMacros.zeroExtend(lh_data),
    ControlMacros.MEM_CTL_LWU -> CommonMacros.zeroExtend(lw_data)
  )
  io.r_data := MuxLookup(io.mem_ctl, 0.U(64.W))(r_data_map)

  io.fin := (!r_en && !w_en) || (r_en && io.r.fire) || (w_en && io.b.fire)
}

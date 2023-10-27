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

  /* ========== Parameter ========== */
  val r_idle :: r_wait_ready :: r_wait_data :: Nil = Enum(3)
  val w_idle :: w_wait_ready :: w_wait_resp :: Nil = Enum(3)

  /* ========== Register ========== */
  val r_state = RegInit(r_idle)
  val w_state = RegInit(w_idle)

  // write to memory
  val w_req = w_en && (w_state === w_idle)

  w_state := MuxLookup(w_state, w_idle)(
    Seq(
      w_idle       -> Mux(w_req, w_wait_ready, w_idle),
      w_wait_ready -> Mux(io.aw.ready && io.w.ready, w_wait_resp, w_wait_ready),
      w_wait_resp  -> Mux(io.b.valid, w_idle, w_wait_resp)
    )
  )

  io.aw.valid     := w_req || (w_state === w_wait_ready)
  io.aw.bits.addr := io.addr
  io.aw.bits.prot := 0.U(3.W)
  io.w.valid      := w_req || (w_state === w_wait_ready)
  io.w.bits.data  := w_data
  io.w.bits.strb  := mask

  io.b.ready := io.b.valid

  // read from memory
  val r_req = r_en && (r_state === r_idle)

  r_state := MuxLookup(r_state, r_idle)(
    Seq(
      r_idle       -> Mux(r_req, r_wait_ready, r_idle),
      r_wait_ready -> Mux(io.ar.ready, r_wait_data, r_wait_ready),
      r_wait_data  -> Mux(io.r.valid, r_idle, r_wait_data)
    )
  )

  io.ar.valid     := r_req || (r_state === r_wait_ready)
  io.ar.bits.addr := io.addr
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

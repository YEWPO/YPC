package utils.loadstore

import chisel3._
import chisel3.util._
import macros._

class DataMemIO extends Bundle {
  val addr    = Input(UInt(64.W))
  val w_data  = Input(UInt(64.W))
  val mem_ctl = Input(UInt(5.W))

  val r_data = Output(UInt(64.W))
}

class DataMem extends Module {
  val io = IO(new DataMemIO)

  val mem_read  = Module(new MemRead)
  val mem_write = Module(new MemWrite)

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

  // write to memory
  mem_write.io.addr   := io.addr
  mem_write.io.w_data := w_data
  mem_write.io.mask   := mask

  // read from memory
  mem_read.io.addr := io.addr
  val r_data = mem_read.io.r_data

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
}

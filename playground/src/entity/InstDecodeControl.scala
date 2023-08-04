package entity

import chisel3._

class InstDecodeControl extends Bundle {
  val a_ctl      = Output(Bool())
  val b_ctl      = Output(Bool())
  val dnpc_ctl   = Output(Bool())
  val alu_ctl    = Output(UInt(5.W))
  val jump_op    = Output(UInt(2.W))
  val mem_ctl    = Output(UInt(5.W))
  val wb_ctl     = Output(UInt(2.W))
  val reg_w_en   = Output(Bool())
  val ebreak_op  = Output(Bool())
  val invalid_op = Output(Bool())
}

package entity

import chisel3._

class ExecuteControl extends Bundle {
  val mem_w_en   = Output(Bool())
  val mem_mask   = Output(UInt(64.W))
  val wb_ctl     = Output(UInt(2.W))
  val reg_w_en   = Output(Bool())
  val ebreak_op  = Output(Bool())
  val invalid_op = Output(Bool())
}

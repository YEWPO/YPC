package entity

import chisel3._

class ExecuteControl extends Bundle {
  val mem_ctl    = Output(UInt(5.W))
  val wb_ctl     = Output(UInt(2.W))
  val reg_w_en   = Output(Bool())
  val ebreak_op  = Output(Bool())
  val invalid_op = Output(Bool())
}

class ExecuteCSRControl extends Bundle {
  val csr_w_en = Output(Bool())
}

package entity

import chisel3._

class InstDecodeControl extends Bundle {
  val a_ctl       = Output(Bool())
  val b_ctl       = Output(Bool())
  val dnpc_ctl    = Output(Bool())
  val alu_ctl     = Output(UInt(5.W))
  val mul_ctl     = Output(UInt(4.W))
  val exe_out_ctl = Output(Bool())
  val jump_op     = Output(UInt(2.W))
  val mem_ctl     = Output(UInt(5.W))
  val wb_ctl      = Output(UInt(2.W))
  val reg_w_en    = Output(Bool())
  val ebreak_op   = Output(Bool())
  val invalid_op  = Output(Bool())
}

class InstDecodeCSRControl extends Bundle {
  val csr_r_en    = Output(Bool())
  val csr_w_en    = Output(Bool())
  val csr_src_ctl = Output(Bool())
  val csr_op_ctl  = Output(UInt(2.W))
}

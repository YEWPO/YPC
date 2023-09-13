package bundles

import chisel3._

class ID2EXDataBundle extends Bundle {
  val imm  = UInt(64.W)
  val src1 = UInt(64.W)
  val src2 = UInt(64.W)
  val rd   = UInt(5.W)
  val inst = UInt(32.W)
  val pc   = UInt(64.W)
  val snpc = UInt(64.W)
}

class ID2EXControlBundle extends Bundle {
  val a_ctl       = Bool()
  val b_ctl       = Bool()
  val dnpc_ctl    = Bool()
  val alu_ctl     = UInt(5.W)
  val mul_ctl     = UInt(4.W)
  val exe_out_ctl = Bool()
  val jump_op     = UInt(2.W)
  val mem_ctl     = UInt(5.W)
  val wb_ctl      = UInt(2.W)
  val reg_w_en    = Bool()
  val ebreak_op   = Bool()
  val invalid_op  = Bool()
}

class ID2EXBundle extends Bundle {
  val data    = new ID2EXDataBundle
  val control = new ID2EXControlBundle
}
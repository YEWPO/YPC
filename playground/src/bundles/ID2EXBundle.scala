package bundles

import chisel3._

class ID2EXDataBundle extends Bundle {
  val imm  = UInt(64.W)
  val src1 = UInt(64.W)
  val src2 = UInt(64.W)
  val rd   = UInt(5.W)
  val inst = UInt(32.W)
  val pc   = UInt(64.W)
  val dnpc = UInt(64.W)
  val snpc = UInt(64.W)

  val csr_data   = UInt(64.W)
  val csr_w_addr = UInt(12.W)
  val csr_uimm   = UInt(64.W)
}

class ID2EXControlBundle extends Bundle {
  val a_ctl       = UInt(2.W)
  val b_ctl       = Bool()
  val dnpc_ctl    = Bool()
  val alu_ctl     = UInt(5.W)
  val mul_ctl     = UInt(4.W)
  val exe_out_ctl = Bool()
  val jump_op     = UInt(2.W)
  val mem_ctl     = UInt(5.W)
  val reg_w_en    = Bool()
  val ebreak_op   = Bool()
  val invalid_op  = Bool()

  val csr_r_en    = Bool()
  val csr_w_en    = Bool()
  val csr_op_ctl  = UInt(2.W)
  val csr_src_ctl = Bool()
}

class ID2EXBundle extends Bundle {
  val data    = new ID2EXDataBundle
  val control = new ID2EXControlBundle
}

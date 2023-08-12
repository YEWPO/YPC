package control

import chisel3._

class CSRControlUnitIO extends Bundle {
  val zicsr_op = Input(Bool())
  val rd = Input(UInt(5.W))
  val rs1 = Input(UInt(5.W))
  val funct = Input(UInt(3.W))

  val csr_r_en = Output(Bool())
  val csr_w_en = Output(Bool())
  val csr_src_ctl = Output(Bool())
  val csr_op_ctl = Output(UInt(2.W))
}

class CSRControlUnit extends Module {
  val io = IO(new CSRControlUnitIO)

  io.csr_r_en := io.zicsr_op && io.rd.orR
  io.csr_w_en := io.zicsr_op && io.rs1.orR
  io.csr_src_ctl := io.funct(2) // the third bit of funct indicate that use uimm as src
  io.csr_op_ctl := io.funct(1, 0) // 1 is rw, 2 is rs, 3 is rc
}

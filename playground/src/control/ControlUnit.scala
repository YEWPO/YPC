package control

import chisel3._
import chisel3.util._

class ControlUnitIO extends Bundle {
  val inst = Input(UInt(32.W))

  val imm_type    = Output(UInt(3.W))
  val rs1_tag     = Output(Bool())
  val rs2_tag     = Output(Bool())
  val a_ctl       = Output(Bool())
  val b_ctl       = Output(Bool())
  val dnpc_ctl    = Output(Bool())
  val alu_ctl     = Output(UInt(5.W))
  val mul_ctl     = Output(UInt(4.W))
  val exe_out_ctl = Output(Bool())
  val mem_ctl     = Output(UInt(5.W))
  val wb_ctl      = Output(UInt(2.W))
  val reg_w_en    = Output(Bool())
  val jump_op     = Output(UInt(2.W))
  val ebreak_op   = Output(Bool())
  val invalid_op  = Output(Bool())
}

object DecodeTableInfo {
  val IMM_TYPE    = 0
  val RS1_TAG     = 1
  val RS2_TAG     = 2
  val A_CTL       = 3
  val B_CTL       = 4
  val DNPC_CTL    = 5
  val ALU_CTL     = 6
  val MUL_CTL     = 7
  val EXE_OUT_CTL = 8
  val MEM_CLT     = 9
  val WB_CTL      = 10
  val REG_W_EN    = 11
  val JUMP_OP     = 12
  val EBREAK_OP   = 13
  val INVALID_OP  = 14
}

class ControlUnit extends Module {
  val io = IO(new ControlUnitIO)

  val decode_result = ListLookup(io.inst, DecodeTable.default_decode, DecodeTable.decode_map)

  io.imm_type    := decode_result(DecodeTableInfo.IMM_TYPE)
  io.rs1_tag     := decode_result(DecodeTableInfo.RS1_TAG)
  io.rs2_tag     := decode_result(DecodeTableInfo.RS2_TAG)
  io.a_ctl       := decode_result(DecodeTableInfo.A_CTL)
  io.b_ctl       := decode_result(DecodeTableInfo.B_CTL)
  io.dnpc_ctl    := decode_result(DecodeTableInfo.DNPC_CTL)
  io.alu_ctl     := decode_result(DecodeTableInfo.ALU_CTL)
  io.mul_ctl     := decode_result(DecodeTableInfo.MUL_CTL)
  io.exe_out_ctl := decode_result(DecodeTableInfo.EXE_OUT_CTL)
  io.mem_ctl     := decode_result(DecodeTableInfo.MEM_CLT)
  io.wb_ctl      := decode_result(DecodeTableInfo.WB_CTL)
  io.reg_w_en    := decode_result(DecodeTableInfo.REG_W_EN)
  io.jump_op     := decode_result(DecodeTableInfo.JUMP_OP)
  io.ebreak_op   := decode_result(DecodeTableInfo.EBREAK_OP)
  io.invalid_op  := decode_result(DecodeTableInfo.INVALID_OP)
}

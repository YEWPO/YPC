package unit

import chisel3._
import control._
import utils._
import chisel3.util._

class InstDecodeUnitIO extends Bundle {
  val enable = Input(Bool())
  val reset  = Input(Bool())

  val inst_f = Input(UInt(32.W))
  val pc_f   = Input(UInt(64.W))
  val snpc_f = Input(UInt(64.W))

  val w_en_w   = Input(Bool())
  val w_data_w = Input(UInt(64.W))
  val rd_w     = Input(UInt(5.W))

  val a_ctl_d    = Output(Bool())
  val b_ctl_d    = Output(Bool())
  val dnpc_ctl_d = Output(Bool())
  val alu_ctl_d  = Output(UInt(5.W))
  val mem_w_en_d = Output(Bool())
  val mem_mask_d = Output(UInt(64.W))
  val wb_ctl_d   = Output(UInt(2.W))
  val reg_w_en_d = Output(Bool())
  val jump_op_d  = Output(UInt(2.W))

  val imm_d     = Output(UInt(64.W))
  val rd_d      = Output(UInt(64.W))
  val r_data1_d = Output(UInt(64.W))
  val r_data2_d = Output(UInt(64.W))
  val pc_d      = Output(UInt(64.W))
  val snpc_d    = Output(UInt(64.W))
}

/**
  * instruction bit map
  *
  *  31    25 24 20 19 15 14    12 11 7 6      0
  * +--------+-----+-----+--------+----+--------+
  * | funct7 | rs2 | rs1 | funct3 | rd | opcode |
  * +--------+-----+-----+--------+----+--------+
  */
class InstDecodeUnit extends Module {
  val io = IO(new InstDecodeUnitIO)

  val control_unit = Module(new ControlUnit)
  val imm_gen      = Module(new ImmGen)
  val reg_files    = Module(new Register)
  val ebreak_part  = Module(new Ebreak)
  val invalid_part = Module(new Invalid)

  withReset(io.reset) {
    val inst_f = RegEnable(io.inst_f, 0.U(32.W), io.enable)
    val pc_f   = RegEnable(io.pc_f, "h8000_0000".U(64.W), io.enable)
    val snpc_f = RegEnable(io.snpc_f, "h8000_0000".U(64.W), io.enable)

    control_unit.io.inst := inst_f

    imm_gen.io.in       := inst_f(31, 7)
    imm_gen.io.imm_type := control_unit.io.imm_type

    reg_files.io.rs1 := inst_f(19, 15)
    reg_files.io.rs2 := inst_f(24, 20)

    /**
      * w_en, w_data and rd will be valid at write back stage
      */
    reg_files.io.rd     := io.rd_w
    reg_files.io.w_en   := io.w_en_w
    reg_files.io.w_data := io.w_data_w

    /**
      * imm = imm_gen(inst)
      * rd = inst[11:7]
      * r_data1 = reg[rs1]
      * r_data2 = reg[rs2]
      */
    io.imm_d     := imm_gen.io.imm_out
    io.rd_d      := inst_f(11, 7)
    io.r_data1_d := reg_files.io.r_data1
    io.r_data2_d := reg_files.io.r_data2
    io.pc_d      := pc_f
    io.snpc_d    := snpc_f

    /**
      * ebreak
      */
    ebreak_part.io.clock := clock
    ebreak_part.io.in    := control_unit.io.ebreak_op

    /**
      * invalid
      */
    invalid_part.io.clock := clock
    invalid_part.io.in    := control_unit.io.invalid_op

    /**
      * control signals
      */
    io.a_ctl_d    := control_unit.io.a_ctl
    io.b_ctl_d    := control_unit.io.b_ctl
    io.dnpc_ctl_d := control_unit.io.dnpc_ctl
    io.alu_ctl_d  := control_unit.io.alu_ctl
    io.mem_w_en_d := control_unit.io.mem_w_en
    io.mem_mask_d := control_unit.io.mem_mask
    io.wb_ctl_d   := control_unit.io.wb_ctl
    io.reg_w_en_d := control_unit.io.reg_w_en
    io.jump_op_d  := control_unit.io.jump_op
  }
}

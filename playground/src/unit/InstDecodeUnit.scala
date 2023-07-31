package unit

import chisel3._
import control._
import utils._
import chisel3.util._
import entity._

/**
  * instruction bit map
  *
  *  31    25 24 20 19 15 14    12 11 7 6      0
  * +--------+-----+-----+--------+----+--------+
  * | funct7 | rs2 | rs1 | funct3 | rd | opcode |
  * +--------+-----+-----+--------+----+--------+
  */
class InstDecodeUnit extends Module {
  val inst_decode_hazard = IO(new InstDecodeHazard)
  val inst_fetch_data = Flipped(IO(new InstFetchData))
  val inst_decode_control = IO(new InstDecodeControl)
  val inst_decode_data = IO(new InstDecodeData)
  val write_back_control = Flipped(IO(new WriteBackControl))
  val write_back_data = Flipped(IO(new WriteBackData))

  val control_unit = Module(new ControlUnit)
  val imm_gen      = Module(new ImmGen)
  val reg_files    = Module(new Register)

  withReset(inst_decode_hazard.reset || reset.asBool) {
    val inst_f = RegEnable(inst_fetch_data.inst, CommonMacro.INST_RESET_VAL, inst_decode_hazard.enable)
    val pc_f   = RegEnable(inst_fetch_data.pc, CommonMacro.PC_RESET_VAL, inst_decode_hazard.enable)
    val snpc_f = RegEnable(inst_fetch_data.snpc, CommonMacro.PC_RESET_VAL, inst_decode_hazard.enable)

    /**
      * control unit
      */
    control_unit.io.inst := inst_f

    /**
      * immediate extend
      */
    imm_gen.io.in       := inst_f(31, 7)
    imm_gen.io.imm_type := control_unit.io.imm_type

    /**
      * register unit
      */
    reg_files.io.rs1 := inst_f(19, 15)
    reg_files.io.rs2 := inst_f(24, 20)

    /**
      * w_en, w_data and rd will be valid at write back stage
      */
    reg_files.io.rd     := write_back_data.rd
    reg_files.io.w_en   := write_back_control.reg_w_en
    reg_files.io.w_data := write_back_data.wb_data

    /**
      * imm = imm_gen(inst)
      * rd = inst[11:7]
      * r_data1 = reg[rs1]
      * r_data2 = reg[rs2]
      */
    inst_decode_data.imm     := imm_gen.io.imm_out
    inst_decode_data.rd      := inst_f(11, 7)
    inst_decode_data.r_data1 := reg_files.io.r_data1
    inst_decode_data.r_data2 := reg_files.io.r_data2
    inst_decode_data.pc      := pc_f
    inst_decode_data.snpc   := snpc_f
    inst_decode_data.inst    := inst_f

    /**
      * control signals
      */
    inst_decode_control.a_ctl      := control_unit.io.a_ctl
    inst_decode_control.b_ctl      := control_unit.io.b_ctl
    inst_decode_control.dnpc_ctl   := control_unit.io.dnpc_ctl
    inst_decode_control.alu_ctl    := control_unit.io.alu_ctl
    inst_decode_control.mem_w_en   := control_unit.io.mem_w_en
    inst_decode_control.mem_mask   := control_unit.io.mem_mask
    inst_decode_control.wb_ctl     := control_unit.io.wb_ctl
    inst_decode_control.reg_w_en   := control_unit.io.reg_w_en
    inst_decode_control.jump_op    := control_unit.io.jump_op
    inst_decode_control.ebreak_op  := control_unit.io.ebreak_op
    inst_decode_control.invalid_op := control_unit.io.invalid_op
  }
}

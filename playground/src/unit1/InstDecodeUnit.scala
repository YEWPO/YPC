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
  val inst_decode_hazard  = IO(new InstDecodeHazard)
  val inst_fetch_data     = IO(Flipped(new InstFetchData))
  val inst_decode_control = IO(new InstDecodeControl)
  val inst_decode_data    = IO(new InstDecodeData)
  val write_back_control  = IO(Flipped(new WriteBackControl))
  val write_back_data     = IO(Flipped(new WriteBackData))
  val execute_forward     = IO(Flipped(new ExecuteForward))
  val load_store_forward  = IO(Flipped(new LoadStoreForward))
  val write_back_forward  = IO(Flipped(new WriteBackForward))

  val control_unit = Module(new ControlUnit)
  val imm_gen      = Module(new ImmGen)
  val reg_files    = Module(new Register)
  val forward_a    = Module(new Forward)
  val forward_b    = Module(new Forward)

  val inst_decode_csr_control = IO(new InstDecodeCSRControl)
  val inst_decode_csr_data    = IO(new InstDecodeCSRData)
  val inst_decode_csr_hazard  = IO(new InstDecodeCSRHazard)
  val write_back_csr_control  = IO(Flipped(new WriteBackCSRControl))
  val write_back_csr_data     = IO(Flipped(new WriteBackCSRData))
  val execute_csr_forward     = IO(Flipped(new ExecuteCSRForward))
  val load_store_csr_forward  = IO(Flipped(new LoadStoreCSRForward))
  val write_back_csr_forward  = IO(Flipped(new WriteBackCSRForward))

  val csr         = Module(new CSR)
  val csr_forward = Module(new CSRForward)
  val csr_control = Module(new CSRControlUnit)

  withReset(inst_decode_hazard.reset || inst_decode_csr_hazard.csr_reset || reset.asBool) {
    val inst = RegEnable(inst_fetch_data.inst, CommonMacro.INST_RESET_VAL, inst_decode_hazard.enable)
    val pc   = RegEnable(inst_fetch_data.pc, CommonMacro.PC_RESET_VAL, inst_decode_hazard.enable)
    val snpc = RegEnable(inst_fetch_data.snpc, CommonMacro.PC_RESET_VAL, inst_decode_hazard.enable)

    /**
      * hazard
      */
    inst_decode_hazard.rs1     := inst(19, 15)
    inst_decode_hazard.rs2     := inst(24, 20)
    inst_decode_hazard.rs1_tag := control_unit.io.rs1_tag
    inst_decode_hazard.rs2_tag := control_unit.io.rs2_tag

    /**
      * control unit
      */
    control_unit.io.inst := inst

    /**
      * immediate extend
      */
    imm_gen.io.in       := inst(31, 7)
    imm_gen.io.imm_type := control_unit.io.imm_type

    /**
      * register unit
      */
    reg_files.io.rs1 := inst(19, 15)
    reg_files.io.rs2 := inst(24, 20)

    /**
      * forward a
      */
    forward_a.io.src     := reg_files.io.r_data1
    forward_a.io.alu_E   := execute_forward.exe_out
    forward_a.io.snpc_E  := execute_forward.snpc
    forward_a.io.alu_M   := load_store_forward.exe_out
    forward_a.io.mem_M   := load_store_forward.mem_out
    forward_a.io.snpc_M  := load_store_forward.snpc
    forward_a.io.wb_data := write_back_forward.wb_data
    forward_a.io.f_ctl   := inst_decode_hazard.fa_ctl

    /**
      * forward b
      */
    forward_b.io.src     := reg_files.io.r_data2
    forward_b.io.alu_E   := execute_forward.exe_out
    forward_b.io.snpc_E  := execute_forward.snpc
    forward_b.io.alu_M   := load_store_forward.exe_out
    forward_b.io.mem_M   := load_store_forward.mem_out
    forward_b.io.snpc_M  := load_store_forward.snpc
    forward_b.io.wb_data := write_back_forward.wb_data
    forward_b.io.f_ctl   := inst_decode_hazard.fb_ctl

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
    inst_decode_data.imm  := imm_gen.io.imm_out
    inst_decode_data.rd   := inst(11, 7)
    inst_decode_data.src1 := forward_a.io.out
    inst_decode_data.src2 := forward_b.io.out
    inst_decode_data.pc   := pc
    inst_decode_data.snpc := Mux(control_unit.io.ecall_op, csr.io.tvec, Mux(control_unit.io.mret_op, csr.io.epc, snpc))
    inst_decode_data.inst := inst

    /**
      * control signals
      */
    inst_decode_control.a_ctl       := control_unit.io.a_ctl
    inst_decode_control.b_ctl       := control_unit.io.b_ctl
    inst_decode_control.dnpc_ctl    := control_unit.io.dnpc_ctl
    inst_decode_control.alu_ctl     := control_unit.io.alu_ctl
    inst_decode_control.mul_ctl     := control_unit.io.mul_ctl
    inst_decode_control.exe_out_ctl := control_unit.io.exe_out_ctl
    inst_decode_control.mem_ctl     := control_unit.io.mem_ctl
    inst_decode_control.wb_ctl      := control_unit.io.wb_ctl
    inst_decode_control.reg_w_en    := control_unit.io.reg_w_en
    inst_decode_control.jump_op     := control_unit.io.jump_op
    inst_decode_control.ebreak_op   := control_unit.io.ebreak_op
    inst_decode_control.invalid_op  := control_unit.io.invalid_op

    /**
      * CSR part
      */
    csr_control.io.zicsr_op := control_unit.io.csr_op
    csr_control.io.rd       := inst(11, 7)
    csr_control.io.rs1      := inst(19, 15)
    csr_control.io.funct    := inst(14, 12)

    csr.io.csr_r_addr := inst(31, 20)
    csr.io.csr_r_en   := csr_control.io.csr_r_en
    csr.io.csr_w_addr := write_back_csr_data.csr_w_addr
    csr.io.csr_w_data := write_back_csr_data.csr_w_data
    csr.io.csr_w_en   := write_back_csr_control.csr_w_en
    csr.io.expt_op    := control_unit.io.ecall_op
    csr.io.pc         := pc

    csr_forward.io.csr_data_D := csr.io.csr_r_data
    csr_forward.io.csr_data_E := execute_csr_forward.csr_exe_out
    csr_forward.io.csr_data_M := load_store_csr_forward.csr_ls_out
    csr_forward.io.csr_data_W := write_back_csr_forward.csr_wb_out
    csr_forward.io.csr_fw_ctl := inst_decode_csr_hazard.csr_forward_ctl

    inst_decode_csr_hazard.csr_r_addr     := inst(31, 20)
    inst_decode_csr_hazard.csr_r_addr_tag := csr_control.io.csr_r_en
    inst_decode_csr_hazard.ecall_op       := control_unit.io.ecall_op
    inst_decode_csr_hazard.mret_op        := control_unit.io.mret_op
    inst_decode_csr_hazard.epc            := csr.io.epc
    inst_decode_csr_hazard.tvec           := csr.io.tvec

    inst_decode_csr_control.csr_r_en    := csr_control.io.csr_r_en
    inst_decode_csr_control.csr_w_en    := csr_control.io.csr_w_en
    inst_decode_csr_control.csr_op_ctl  := csr_control.io.csr_op_ctl
    inst_decode_csr_control.csr_src_ctl := csr_control.io.csr_src_ctl

    inst_decode_csr_data.csr_data   := csr_forward.io.csr_data_out
    inst_decode_csr_data.csr_w_addr := inst(31, 20)
    inst_decode_csr_data.csr_uimm   := CommonMacro.zeroExtend(inst(19, 15))
  }
}

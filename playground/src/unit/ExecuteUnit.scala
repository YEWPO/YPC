package unit

import chisel3._
import entity._
import utils._
import control._

class ExecuteUnit extends Module {
  val inst_decode_data    = IO(Flipped(new InstDecodeData))
  val inst_decode_control = IO(Flipped(new InstDecodeControl))
  val execute_hazard      = IO(new ExecuteHazard)
  val execute_data        = IO(new ExecuteData)
  val execute_control     = IO(new ExecuteControl)
  val execute_forward     = IO(new ExecuteForward)
  val dnpc                = IO(Output(UInt(64.W)))
  val jump_ctl            = IO(Output(Bool()))

  val alu = Module(new AlgLog)

  withReset(execute_hazard.reset || reset.asBool) {
    // data registers
    val snpc    = RegNext(inst_decode_data.snpc, CommonMacro.PC_RESET_VAL)
    val pc      = RegNext(inst_decode_data.pc, CommonMacro.PC_RESET_VAL)
    val inst    = RegNext(inst_decode_data.inst, CommonMacro.INST_RESET_VAL)
    val rd      = RegNext(inst_decode_data.rd, 0.U)
    val r_data1 = RegNext(inst_decode_data.r_data1, 0.U)
    val r_data2 = RegNext(inst_decode_data.r_data2, 0.U)
    val imm     = RegNext(inst_decode_data.imm, 0.U)

    // control registers
    val a_ctl      = RegNext(inst_decode_control.a_ctl, ControlMacro.A_CTL_DEFAULT)
    val b_ctl      = RegNext(inst_decode_control.b_ctl, ControlMacro.B_CTL_DEFAULT)
    val dnpc_ctl   = RegNext(inst_decode_control.dnpc_ctl, ControlMacro.DNPC_CTL_DEFAULT)
    val alu_ctl    = RegNext(inst_decode_control.alu_ctl, ControlMacro.ALU_CTL_DEFAULT)
    val jump_op    = RegNext(inst_decode_control.jump_op, ControlMacro.JUMP_OP_DEFAULT)
    val mem_ctl    = RegNext(inst_decode_control.mem_ctl, ControlMacro.MEM_CTL_DEFAULT)
    val wb_ctl     = RegNext(inst_decode_control.wb_ctl, ControlMacro.WB_CTL_DEFAULT)
    val reg_w_en   = RegNext(inst_decode_control.reg_w_en, ControlMacro.REG_W_DISABLE)
    val ebreak_op  = RegNext(inst_decode_control.ebreak_op, ControlMacro.EBREAK_OP_NO)
    val invalid_op = RegNext(inst_decode_control.invalid_op, ControlMacro.INVALID_OP_NO)

    /**
      * first situation: a branch operation and condition is true
      * second situation; a jump operation
      */
    val jump_sig =
      (jump_op === ControlMacro.JUMP_OP_JAL) || ((jump_op === ControlMacro.JUMP_OP_BRANCH) && alu.io.alu_out(0).andR)
    jump_ctl := jump_sig

    // dynamic next pc
    dnpc := Mux(dnpc_ctl, r_data2, pc) + imm

    // hazard part
    execute_hazard.jump_sig := jump_sig
    execute_hazard.wb_ctl   := wb_ctl
    execute_hazard.rd       := rd
    execute_hazard.rd_tag   := reg_w_en

    // forward part
    execute_forward.alu_out := alu.io.alu_out
    execute_forward.snpc    := snpc

    // algorithm logic unit
    alu.io.src1    := Mux(a_ctl, pc, r_data1)
    alu.io.src2    := Mux(b_ctl, r_data2, imm)
    alu.io.alu_ctl := alu_ctl

    /**
      * output data
      *
      * alu_out = alu(src1, src2)
      */
    execute_data.snpc    := snpc
    execute_data.pc      := pc
    execute_data.inst    := inst
    execute_data.rd      := rd
    execute_data.r_data2 := r_data2
    execute_data.alu_out := alu.io.alu_out

    /**
      * control signals
      */
    execute_control.mem_ctl    := mem_ctl
    execute_control.wb_ctl     := wb_ctl
    execute_control.reg_w_en   := reg_w_en
    execute_control.ebreak_op  := ebreak_op
    execute_control.invalid_op := invalid_op
  }
}

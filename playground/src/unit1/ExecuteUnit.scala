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
  val mul = Module(new MulDiv)

  val inst_decode_csr_data    = IO(Flipped(new InstDecodeCSRData))
  val inst_decode_csr_control = IO(Flipped(new InstDecodeCSRControl))
  val execute_csr_data        = IO(new ExecuteCSRData)
  val execute_csr_control     = IO(new ExecuteCSRControl)
  val execute_csr_hazard      = IO(new ExecuteCSRHazard)
  val execute_csr_forward     = IO(new ExecuteCSRForward)

  val csr_operator = Module(new CSROperation)

  withReset(execute_hazard.reset || reset.asBool) {
    // data registers
    val snpc = RegNext(inst_decode_data.snpc, CommonMacro.PC_RESET_VAL)
    val pc   = RegNext(inst_decode_data.pc, CommonMacro.PC_RESET_VAL)
    val inst = RegNext(inst_decode_data.inst, CommonMacro.INST_RESET_VAL)
    val rd   = RegNext(inst_decode_data.rd, 0.U)
    val src1 = RegNext(inst_decode_data.src1, 0.U)
    val src2 = RegNext(inst_decode_data.src2, 0.U)
    val imm  = RegNext(inst_decode_data.imm, 0.U)

    // control registers
    val a_ctl       = RegNext(inst_decode_control.a_ctl, ControlMacro.A_CTL_DEFAULT)
    val b_ctl       = RegNext(inst_decode_control.b_ctl, ControlMacro.B_CTL_DEFAULT)
    val dnpc_ctl    = RegNext(inst_decode_control.dnpc_ctl, ControlMacro.DNPC_CTL_DEFAULT)
    val alu_ctl     = RegNext(inst_decode_control.alu_ctl, ControlMacro.ALU_CTL_DEFAULT)
    val mul_ctl     = RegNext(inst_decode_control.mul_ctl, ControlMacro.MUL_CTL_DEFAULT)
    val exe_out_ctl = RegNext(inst_decode_control.exe_out_ctl, ControlMacro.EXE_OUT_DEFAULT)
    val jump_op     = RegNext(inst_decode_control.jump_op, ControlMacro.JUMP_OP_DEFAULT)
    val mem_ctl     = RegNext(inst_decode_control.mem_ctl, ControlMacro.MEM_CTL_DEFAULT)
    val wb_ctl      = RegNext(inst_decode_control.wb_ctl, ControlMacro.WB_CTL_DEFAULT)
    val reg_w_en    = RegNext(inst_decode_control.reg_w_en, ControlMacro.REG_W_DISABLE)
    val ebreak_op   = RegNext(inst_decode_control.ebreak_op, ControlMacro.EBREAK_OP_NO)
    val invalid_op  = RegNext(inst_decode_control.invalid_op, ControlMacro.INVALID_OP_NO)

    /**
      * CSR control or data registers
      */
    val csr_data    = RegNext(inst_decode_csr_data.csr_data, 0.U(64.W))
    val csr_uimm    = RegNext(inst_decode_csr_data.csr_uimm, 0.U(64.W))
    val csr_w_addr  = RegNext(inst_decode_csr_data.csr_w_addr, 0.U(12.W))
    val csr_r_en    = RegNext(inst_decode_csr_control.csr_r_en, false.B)
    val csr_w_en    = RegNext(inst_decode_csr_control.csr_w_en, false.B)
    val csr_src_ctl = RegNext(inst_decode_csr_control.csr_src_ctl, false.B)
    val csr_op_ctl  = RegNext(inst_decode_csr_control.csr_op_ctl, 0.U(2.W))

    /**
      * first situation: a branch operation and condition is true
      * second situation; a jump operation
      */
    val jump_sig =
      (jump_op === ControlMacro.JUMP_OP_JAL) || ((jump_op === ControlMacro.JUMP_OP_BRANCH) && alu.io.alu_out(0).andR)
    jump_ctl := jump_sig

    // dynamic next pc
    val dnpc_val = Mux(dnpc_ctl, src1, pc) + imm
    dnpc := dnpc_val

    // hazard part
    execute_hazard.jump_sig := jump_sig
    execute_hazard.wb_ctl   := wb_ctl
    execute_hazard.rd       := rd
    execute_hazard.rd_tag   := reg_w_en

    // algorithm logic unit
    alu.io.src1    := Mux(csr_r_en, csr_data, Mux(a_ctl, pc, src1))
    alu.io.src2    := Mux(b_ctl, src2, imm)
    alu.io.alu_ctl := alu_ctl

    // multiplication division unit
    mul.io.src1    := src1
    mul.io.src2    := src2
    mul.io.mul_ctl := mul_ctl

    val exe_out = Mux(exe_out_ctl, mul.io.mul_out, alu.io.alu_out)

    // forward part
    execute_forward.exe_out := exe_out
    execute_forward.snpc    := snpc

    /**
      * output data
      *
      * alu_out = alu(src1, src2)
      */
    execute_data.snpc    := snpc
    execute_data.dnpc    := Mux(jump_sig, dnpc_val, snpc)
    execute_data.pc      := pc
    execute_data.inst    := inst
    execute_data.rd      := rd
    execute_data.src2    := src2
    execute_data.exe_out := exe_out

    /**
      * control signals
      */
    execute_control.mem_ctl    := mem_ctl
    execute_control.wb_ctl     := wb_ctl
    execute_control.reg_w_en   := reg_w_en
    execute_control.ebreak_op  := ebreak_op
    execute_control.invalid_op := invalid_op

    /**
      * CSR part
      */
    csr_operator.io.csr_data   := csr_data
    csr_operator.io.src        := Mux(csr_src_ctl, csr_uimm, src1)
    csr_operator.io.csr_op_ctl := csr_op_ctl

    execute_csr_data.csr_w_addr  := csr_w_addr
    execute_csr_data.csr_w_data  := csr_operator.io.csr_op_out
    execute_csr_control.csr_w_en := csr_w_en

    execute_csr_hazard.csr_w_addr     := csr_w_addr
    execute_csr_hazard.csr_w_addr_tag := csr_w_en
    execute_csr_forward.csr_exe_out   := csr_operator.io.csr_op_out
  }
}

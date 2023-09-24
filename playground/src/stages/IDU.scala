package stages

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import bundles._
import control._
import utils.instdecode._
import utils.instdecode._
import bundles.instdecode._
import bundles.writeback._
import macros._

/*
 * instruction bit map
 *
 *  31    25 24 20 19 15 14    12 11 7 6      0
 * +--------+-----+-----+--------+----+--------+
 * | funct7 | rs2 | rs1 | funct3 | rd | opcode |
 * +--------+-----+-----+--------+----+--------+
 */
class IDUIO extends Bundle {
  val if2id = Flipped(Decoupled(new IF2IDBundle))
  val id2ex = Decoupled(new ID2EXBundle)
  val in = Input(new Bundle {
    val wb_data = new WB2RegBundle
  })
  val out = Output(new Bundle {})
}

class IDU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new IDUIO)

  /* ========== Module ========== */
  val control_unit = Module(new ControlUnit)
  val imm_gen      = Module(new ImmGen)
  val gpr          = Module(new GPR)
  val gpr_forward  = Module(new GPRForward)
  val csr          = Module(new CSR)
  val csr_forward  = Module(new CSRForward)
  val csr_control  = Module(new CSRControlUnit)

  /* ========== Parameter ========== */
  val if2id_rst_val = (new IF2IDBundle).Lit(
    _.data -> (new IF2IDDataBundle).Lit(
      _.pc   -> CommonMacros.PC_RESET_VAL,
      _.snpc -> CommonMacros.PC_RESET_VAL,
      _.inst -> CommonMacros.INST_RESET_VAL
    )
  )
  val id2ex_rst_val = (new ID2EXBundle).Lit(
    _.data -> (new ID2EXDataBundle).Lit(
      _.pc         -> CommonMacros.PC_RESET_VAL,
      _.snpc       -> CommonMacros.PC_RESET_VAL,
      _.dnpc       -> CommonMacros.PC_RESET_VAL,
      _.inst       -> CommonMacros.INST_RESET_VAL,
      _.rd         -> 0.U,
      _.src1       -> 0.U,
      _.src2       -> 0.U,
      _.imm        -> 0.U,
      _.csr_data   -> 0.U,
      _.csr_uimm   -> 0.U,
      _.csr_w_addr -> 0.U
    ),
    _.control -> (new ID2EXControlBundle).Lit(
      _.a_ctl       -> ControlMacros.A_CTL_DEFAULT,
      _.b_ctl       -> ControlMacros.B_CTL_DEFAULT,
      _.dnpc_ctl    -> ControlMacros.DNPC_CTL_DEFAULT,
      _.alu_ctl     -> ControlMacros.ALU_CTL_DEFAULT,
      _.mul_ctl     -> ControlMacros.MUL_CTL_DEFAULT,
      _.exe_out_ctl -> ControlMacros.EXE_OUT_DEFAULT,
      _.jump_op     -> ControlMacros.JUMP_OP_DEFAULT,
      _.mem_ctl     -> ControlMacros.MEM_CTL_DEFAULT,
      _.reg_w_en    -> ControlMacros.REG_W_DISABLE,
      _.ebreak_op   -> ControlMacros.EBREAK_OP_NO,
      _.invalid_op  -> ControlMacros.INVALID_OP_NO,
      _.csr_r_en    -> false.B,
      _.csr_w_en    -> false.B,
      _.csr_src_ctl -> false.B,
      _.csr_op_ctl  -> 0.U
    )
  )

  /* ========== Register ========== */
  val r_valid = RegInit(false.B)
  val r_id2ex = RegInit(id2ex_rst_val)

  /* ========== Wire ========== */
  val ready_next   = io.if2id.valid && !io.if2id.ready && (!io.id2ex.valid || io.id2ex.ready)
  val valid_enable = io.if2id.valid && !io.if2id.ready && (!io.id2ex.valid || io.id2ex.ready)
  val valid_next   = r_valid && !io.id2ex.fire
  val if2id_data   = Wire(new IF2IDBundle)

  val dnpc = Mux(
    control_unit.io.ecall_op,
    csr.io.tvec,
    Mux(control_unit.io.mret_op, csr.io.epc, if2id_data.data.snpc)
  )

  /* ========== Sequential Circuit ========== */
  r_valid := Mux(valid_enable, io.if2id.valid, valid_next)

  r_id2ex.data.imm            := imm_gen.io.imm_out
  r_id2ex.data.rd             := if2id_data.data.inst(11, 7)
  r_id2ex.data.src1           := gpr_forward.io.src1
  r_id2ex.data.src2           := gpr_forward.io.src2
  r_id2ex.data.pc             := if2id_data.data.pc
  r_id2ex.data.dnpc           := dnpc
  r_id2ex.data.snpc           := if2id_data.data.snpc
  r_id2ex.data.inst           := if2id_data.data.inst
  r_id2ex.control.a_ctl       := control_unit.io.a_ctl
  r_id2ex.control.b_ctl       := control_unit.io.b_ctl
  r_id2ex.control.dnpc_ctl    := control_unit.io.dnpc_ctl
  r_id2ex.control.alu_ctl     := control_unit.io.alu_ctl
  r_id2ex.control.mul_ctl     := control_unit.io.mul_ctl
  r_id2ex.control.exe_out_ctl := control_unit.io.exe_out_ctl
  r_id2ex.control.mem_ctl     := control_unit.io.mem_ctl
  r_id2ex.control.reg_w_en    := control_unit.io.reg_w_en
  r_id2ex.control.jump_op     := control_unit.io.jump_op
  r_id2ex.control.ebreak_op   := control_unit.io.ebreak_op
  r_id2ex.control.invalid_op  := control_unit.io.invalid_op

  r_id2ex.data.csr_data       := csr_forward.io.src
  r_id2ex.data.csr_w_addr     := if2id_data.data.inst(31, 20)
  r_id2ex.data.csr_uimm       := CommonMacros.zeroExtend(if2id_data.data.inst(19, 15))
  r_id2ex.control.csr_r_en    := csr_control.io.csr_r_en
  r_id2ex.control.csr_w_en    := csr_control.io.csr_w_en
  r_id2ex.control.csr_op_ctl  := csr_control.io.csr_op_ctl
  r_id2ex.control.csr_src_ctl := csr_control.io.csr_src_ctl

  /* ========== Combinational Circuit ========== */
  io.if2id.ready := ready_next
  io.id2ex.valid := r_valid

  if2id_data := Mux(io.if2id.valid, io.if2id.bits, if2id_rst_val)

  io.id2ex.bits := r_id2ex

  control_unit.io.inst    := if2id_data.data.inst
  imm_gen.io.in           := if2id_data.data.inst(31, 7)
  imm_gen.io.imm_type     := control_unit.io.imm_type
  gpr.io.rs1              := if2id_data.data.inst(19, 15)
  gpr.io.rs2              := if2id_data.data.inst(24, 20)
  gpr.io.rd               := io.in.wb_data.rd
  gpr.io.w_en             := io.in.wb_data.reg_w_en
  gpr.io.w_data           := io.in.wb_data.wb_data
  gpr_forward.io.data1    := gpr.io.r_data1
  gpr_forward.io.data2    := gpr.io.r_data2
  gpr_forward.io.forward  := io.in.forward
  gpr_forward.io.fw_ctl   := io.in.hazard
  csr.io.csr_r_addr       := if2id_data.data.inst(31, 20)
  csr.io.csr_r_en         := csr_control.io.csr_r_en
  csr.io.csr_w_addr       := io.in.wb_data.csr_w_addr
  csr.io.csr_w_data       := io.in.wb_data.csr_w_data
  csr.io.csr_w_en         := io.in.wb_data.csr_w_en
  csr.io.expt_op          := control_unit.io.ecall_op
  csr.io.pc               := if2id_data.data.pc
  csr_control.io.zicsr_op := control_unit.io.csr_op
  csr_control.io.rd       := if2id_data.data.inst(11, 7)
  csr_control.io.rs1      := if2id_data.data.inst(19, 15)
  csr_control.io.funct    := if2id_data.data.inst(14, 12)
  csr_forward.io.data     := csr.io.csr_r_data
  csr_forward.io.forward  := io.in.csr_forward
  csr_forward.io.fw_ctl   := io.in.csr_hazard.csr_forward_ctl

  io.out.hazard.rs1                := if2id_data.data.inst(19, 15)
  io.out.hazard.rs2                := if2id_data.data.inst(24, 20)
  io.out.hazard.rs1_tag            := control_unit.io.rs1_tag
  io.out.hazard.rs2_tag            := control_unit.io.rs2_tag
  io.out.csr_hazard.csr_r_addr     := if2id_data.data.inst(31, 20)
  io.out.csr_hazard.csr_r_addr_tag := csr_control.io.csr_r_en
  io.out.csr_hazard.ecall_op       := control_unit.io.ecall_op
  io.out.csr_hazard.mret_op        := control_unit.io.mret_op
  io.out.csr_hazard.epc            := csr.io.epc
  io.out.csr_hazard.tvec           := csr.io.tvec
}

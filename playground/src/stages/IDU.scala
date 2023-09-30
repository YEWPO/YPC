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
    val wb_data     = new WB2RegBundle
    val gpr_fw_info = new GPRForwardInfo
    val csr_fw_info = new CSRForwardInfo
    val mem_r_op_E  = Bool()
    val mem_r_op_M  = Bool()

    val jump_ctl = Bool()

    val cause = UInt(64.W)
    val epc   = UInt(64.W)
  })
  val out = Output(new Bundle {
    val tvec = UInt(64.W)
  })
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

  /* ========== Register ========== */
  val r_valid = RegInit(false.B)
  val r_id2ex = RegInit(ID2EXBundle.id2ex_rst_val)

  /* ========== Function ========== */
  def mem_r_related(rs: UInt) =
    (rs.orR && (rs === io.in.gpr_fw_info.rd_E) && io.in.mem_r_op_E) || (rs.orR && (rs === io.in.gpr_fw_info.rd_M) && io.in.mem_r_op_M)

  /* ========== Wire ========== */
  val ready_next   = io.if2id.valid && (!io.id2ex.valid || io.id2ex.ready)
  val valid_enable = io.if2id.valid && (!io.id2ex.valid || io.id2ex.ready)
  val valid_next   = r_valid && !io.id2ex.fire
  val if2id_data   = Wire(new IF2IDBundle)

  val valid_current =
    io.if2id.valid && !io.in.jump_ctl && (io.in.cause === CommonMacros.CAUSE_RESET_VAL)

  val dnpc = Mux(control_unit.io.mret_op, csr.io.r_epc, if2id_data.data.snpc)

  val rs1      = if2id_data.data.inst(19, 15)
  val rs2      = if2id_data.data.inst(24, 20)
  val rd       = if2id_data.data.inst(11, 7)
  val funct    = if2id_data.data.inst(14, 12)
  val csr_addr = if2id_data.data.inst(31, 20)

  val mem_r_related_op =
    mem_r_related(Mux(control_unit.io.rs1_tag, rs1, 0.U(5.W))) ||
      mem_r_related(Mux(control_unit.io.rs2_tag, rs2, 0.U(5.W)))

  val cause = Mux(
    if2id_data.data.cause === CommonMacros.CAUSE_RESET_VAL,
    Mux(control_unit.io.ecall_op, "hb".U(64.W), CommonMacros.CAUSE_RESET_VAL),
    CommonMacros.CAUSE_RESET_VAL
  )

  /* ========== Sequential Circuit ========== */
  r_valid := Mux(valid_enable, valid_current, valid_next) && !mem_r_related_op

  r_id2ex.data.imm            := imm_gen.io.imm_out
  r_id2ex.data.rd             := rd
  r_id2ex.data.src1           := gpr_forward.io.src1
  r_id2ex.data.src2           := gpr_forward.io.src2
  r_id2ex.data.pc             := if2id_data.data.pc
  r_id2ex.data.dnpc           := dnpc
  r_id2ex.data.snpc           := if2id_data.data.snpc
  r_id2ex.data.inst           := if2id_data.data.inst
  r_id2ex.data.cause          := cause
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
  r_id2ex.control.mret_op     := control_unit.io.mret_op

  r_id2ex.data.csr_data       := csr_forward.io.src
  r_id2ex.data.csr_w_addr     := csr_addr
  r_id2ex.data.csr_uimm       := CommonMacros.zeroExtend(rs1)
  r_id2ex.control.csr_r_en    := csr_control.io.csr_r_en
  r_id2ex.control.csr_w_en    := csr_control.io.csr_w_en
  r_id2ex.control.csr_op_ctl  := csr_control.io.csr_op_ctl
  r_id2ex.control.csr_src_ctl := csr_control.io.csr_src_ctl

  /* ========== Combinational Circuit ========== */
  io.if2id.ready := ready_next && !mem_r_related_op
  io.id2ex.valid := r_valid

  if2id_data := Mux(valid_current, io.if2id.bits, IF2IDBundle.if2id_rst_val)

  io.id2ex.bits := r_id2ex

  io.out.tvec := csr.io.r_tvec

  control_unit.io.inst    := if2id_data.data.inst
  imm_gen.io.in           := if2id_data.data.inst(31, 7)
  imm_gen.io.imm_type     := control_unit.io.imm_type
  gpr.io.rs1              := rs1
  gpr.io.rs2              := rs2
  gpr.io.rd               := io.in.wb_data.rd
  gpr.io.w_en             := io.in.wb_data.reg_w_en
  gpr.io.w_data           := io.in.wb_data.reg_w_data
  gpr_forward.io.data1    := gpr.io.r_data1
  gpr_forward.io.data2    := gpr.io.r_data2
  gpr_forward.io.rs1      := Mux(control_unit.io.rs1_tag, rs1, 0.U(5.W))
  gpr_forward.io.rs2      := Mux(control_unit.io.rs2_tag, rs2, 0.U(5.W))
  gpr_forward.io.fw_info  := io.in.gpr_fw_info
  csr.io.csr_r_addr       := csr_addr
  csr.io.csr_r_en         := csr_control.io.csr_r_en
  csr.io.csr_w_addr       := io.in.wb_data.csr_w_addr
  csr.io.csr_w_data       := io.in.wb_data.csr_w_data
  csr.io.csr_w_en         := io.in.wb_data.csr_w_en
  csr.io.cause            := io.in.cause
  csr.io.w_epc            := io.in.epc
  csr_control.io.zicsr_op := control_unit.io.csr_op
  csr_control.io.rd       := rd
  csr_control.io.rs1      := rs1
  csr_control.io.funct    := funct
  csr_forward.io.data     := csr.io.csr_r_data
  csr_forward.io.addr     := Mux(csr_control.io.csr_r_en, csr_addr, 0.U(12.W))
  csr_forward.io.fw_info  := io.in.csr_fw_info
}

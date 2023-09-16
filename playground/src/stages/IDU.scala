package stages

import chisel3._
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
  val in = Input(new Bundle {
    val data        = new IF2IDDataBundle
    val wb_data     = new WB2RegBundle
    val control     = new IF2IDControlBundle
    val hazard      = new IDHazardControlBundle
    val csr_hazard  = new IDCSRHazardControlBundle
    val forward     = new IDForwardBundle
    val csr_forward = new IDCSRForwardBundle
  })
  val out = Output(new Bundle {
    val data       = new ID2EXDataBundle
    val control    = new ID2EXControlBundle
    val hazard     = new IDHazardDataBundle
    val csr_hazard = new IDCSRHazardDataBundle
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

  /* ========== Combinational Circuit ========== */
  control_unit.io.inst      := io.in.data.inst
  imm_gen.io.in             := io.in.data.inst(31, 7)
  imm_gen.io.imm_type       := control_unit.io.imm_type
  gpr.io.rs1                := io.in.data.inst(19, 15)
  gpr.io.rs2                := io.in.data.inst(24, 20)
  gpr.io.rd                 := io.in.wb_data.rd
  gpr.io.w_en               := io.in.wb_data.reg_w_en
  gpr.io.w_data             := io.in.wb_data.wb_data
  gpr_forward.io.data1      := gpr.io.r_data1
  gpr_forward.io.data2      := gpr.io.r_data2
  gpr_forward.io.exe_E      := io.in.forward.exe_E
  gpr_forward.io.snpc_E     := io.in.forward.snpc_E
  gpr_forward.io.exe_M      := io.in.forward.exe_M
  gpr_forward.io.mem_M      := io.in.forward.mem_M
  gpr_forward.io.snpc_M     := io.in.forward.snpc_M
  gpr_forward.io.wb_data    := io.in.forward.wb_data
  gpr_forward.io.fa_ctl     := io.in.hazard.fa_ctl
  gpr_forward.io.fb_ctl     := io.in.hazard.fb_ctl
  csr.io.csr_r_addr         := io.in.data.inst(31, 20)
  csr.io.csr_r_en           := csr_control.io.csr_r_en
  csr.io.csr_w_addr         := io.in.wb_data.csr_w_addr
  csr.io.csr_w_data         := io.in.wb_data.csr_w_data
  csr.io.csr_w_en           := io.in.wb_data.csr_w_en
  csr.io.expt_op            := control_unit.io.ecall_op
  csr.io.pc                 := io.in.data.pc
  csr_control.io.zicsr_op   := control_unit.io.csr_op
  csr_control.io.rd         := io.in.data.inst(11, 7)
  csr_control.io.rs1        := io.in.data.inst(19, 15)
  csr_control.io.funct      := io.in.data.inst(14, 12)
  csr_forward.io.csr_data_D := csr.io.csr_r_data
  csr_forward.io.csr_data_E := io.in.csr_forward.csr_data_E
  csr_forward.io.csr_data_M := io.in.csr_forward.csr_data_M
  csr_forward.io.csr_data_W := io.in.csr_forward.csr_data_W
  csr_forward.io.csr_fw_ctl := io.in.csr_hazard.csr_forward_ctl

  io.out.data.imm            := imm_gen.io.imm_out
  io.out.data.rd             := io.in.data.inst(11, 7)
  io.out.data.src1           := gpr_forward.io.src1
  io.out.data.src2           := gpr_forward.io.src2
  io.out.data.pc             := io.in.data.pc
  io.out.data.snpc           := io.in.data.snpc
  io.out.data.inst           := io.in.data.inst
  io.out.control.a_ctl       := control_unit.io.a_ctl
  io.out.control.b_ctl       := control_unit.io.b_ctl
  io.out.control.dnpc_ctl    := control_unit.io.dnpc_ctl
  io.out.control.alu_ctl     := control_unit.io.alu_ctl
  io.out.control.mul_ctl     := control_unit.io.mul_ctl
  io.out.control.exe_out_ctl := control_unit.io.exe_out_ctl
  io.out.control.mem_ctl     := control_unit.io.mem_ctl
  io.out.control.wb_ctl      := control_unit.io.wb_ctl
  io.out.control.reg_w_en    := control_unit.io.reg_w_en
  io.out.control.jump_op     := control_unit.io.jump_op
  io.out.control.ebreak_op   := control_unit.io.ebreak_op
  io.out.control.invalid_op  := control_unit.io.invalid_op

  io.out.data.csr_data       := csr_forward.io.csr_data_out
  io.out.data.csr_w_addr     := io.in.data.inst(31, 20)
  io.out.data.csr_uimm       := CommonMacros.zeroExtend(io.in.data.inst(19, 15))
  io.out.control.csr_r_en    := csr_control.io.csr_r_en
  io.out.control.csr_w_en    := csr_control.io.csr_w_en
  io.out.control.csr_op_ctl  := csr_control.io.csr_op_ctl
  io.out.control.csr_src_ctl := csr_control.io.csr_src_ctl

  io.out.hazard.rs1                := io.in.data.inst(19, 15)
  io.out.hazard.rs2                := io.in.data.inst(24, 20)
  io.out.hazard.rs1_tag            := control_unit.io.rs1_tag
  io.out.hazard.rs2_tag            := control_unit.io.rs2_tag
  io.out.csr_hazard.csr_r_addr     := io.in.data.inst(31, 20)
  io.out.csr_hazard.csr_r_addr_tag := csr_control.io.csr_r_en
  io.out.csr_hazard.ecall_op       := control_unit.io.ecall_op
  io.out.csr_hazard.mret_op        := control_unit.io.mret_op
  io.out.csr_hazard.epc            := csr.io.epc
  io.out.csr_hazard.tvec           := csr.io.tvec
}

import chisel3._
import chisel3.util._
import utils._
import unit._
import hazard._

class Top extends Module {
  val io = IO(new Bundle {
    val imm     = Output(UInt(64.W))
    val rd      = Output(UInt(5.W))
    val r_data1 = Output(UInt(64.W))
    val r_data2 = Output(UInt(64.W))

    val pc   = Output(UInt(64.W))
    val snpc = Output(UInt(64.W))

    val a_ctl    = Output(Bool())
    val b_ctl    = Output(Bool())
    val dnpc_ctl = Output(Bool())
    val alu_ctl  = Output(UInt(5.W))
    val mem_w_en = Output(Bool())
    val mem_mask = Output(UInt(64.W))
    val wb_ctl   = Output(UInt(2.W))
    val reg_w_en = Output(Bool())
    val jump_op  = Output(UInt(2.W))
  })

  val hazard_unit = Module(new HazardUnit)

  // ========== Instruction Fetch Unit ============
  val inst_fetch_unit = Module(new InstFetchUnit)
  inst_fetch_unit.io.enable := hazard_unit.io.enable_f

  inst_fetch_unit.io.npc := inst_fetch_unit.io.snpc_f

  // ========== Instruction Decode Unit ============
  val inst_decode_unit = Module(new InstDecodeUnit)
  inst_decode_unit.io.enable := hazard_unit.io.enable_d
  inst_decode_unit.io.reset  := hazard_unit.io.reset_d

  inst_decode_unit.io.inst_f   := inst_fetch_unit.io.inst_f
  inst_decode_unit.io.pc_f     := inst_fetch_unit.io.pc_f
  inst_decode_unit.io.snpc_f   := inst_fetch_unit.io.snpc_f
  inst_decode_unit.io.rd_w     := 0.U
  inst_decode_unit.io.w_en_w   := false.B
  inst_decode_unit.io.w_data_w := 0.U

  io.imm     := inst_decode_unit.io.imm_d
  io.rd      := inst_decode_unit.io.rd_d
  io.r_data1 := inst_decode_unit.io.r_data1_d
  io.r_data2 := inst_decode_unit.io.r_data2_d
  io.pc      := inst_decode_unit.io.pc_d
  io.snpc    := inst_decode_unit.io.snpc_d

  io.a_ctl    := inst_decode_unit.io.a_ctl_d
  io.b_ctl    := inst_decode_unit.io.b_ctl_d
  io.dnpc_ctl := inst_decode_unit.io.dnpc_ctl_d
  io.alu_ctl  := inst_decode_unit.io.alu_ctl_d
  io.mem_w_en := inst_decode_unit.io.mem_w_en_d
  io.mem_mask := inst_decode_unit.io.mem_mask_d
  io.wb_ctl   := inst_decode_unit.io.wb_ctl_d
  io.reg_w_en := inst_decode_unit.io.reg_w_en_d
  io.jump_op  := inst_decode_unit.io.jump_op_d
}

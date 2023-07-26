import chisel3._
import chisel3.util._
import utils._
import unit._
import hazard._

class Top extends Module {
  val io = IO(new Bundle {
    val imm = Output(UInt(64.W))
    val rd = Output(UInt(5.W))
    val r_data1 = Output(UInt(64.W))
    val r_data2 = Output(UInt(64.W))

    val pc = Output(UInt(64.W))
    val snpc = Output(UInt(64.W))
  })

  val hazard_unit = Module(new HazardUnit())

  // ========== Instruction Fetch Unit ============
  val inst_fetch_unit = Module(new InstFetchUnit())
  inst_fetch_unit.io.enable := hazard_unit.io.enable_f

  inst_fetch_unit.io.npc := inst_fetch_unit.io.snpc_f

  // ========== Instruction Decode Unit ============
  val inst_decode_unit = Module(new InstDecodeUnit())
  inst_decode_unit.io.enable := hazard_unit.io.enable_d
  inst_decode_unit.io.reset := hazard_unit.io.reset_d

  inst_decode_unit.io.inst_f := inst_fetch_unit.io.inst_f
  inst_decode_unit.io.pc_f := inst_fetch_unit.io.pc_f
  inst_decode_unit.io.snpc_f := inst_fetch_unit.io.snpc_f
  inst_decode_unit.io.rd_w := 0.U
  inst_decode_unit.io.w_en_w := false.B
  inst_decode_unit.io.w_data_w := 0.U

  io.imm := inst_decode_unit.io.imm_d
  io.rd := inst_decode_unit.io.rd_d
  io.r_data1 := inst_decode_unit.io.r_data1_d
  io.r_data2 := inst_decode_unit.io.r_data2_d
  io.pc := inst_decode_unit.io.pc_d
  io.snpc := inst_decode_unit.io.snpc_d
}

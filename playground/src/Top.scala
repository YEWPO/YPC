import chisel3._
import chisel3.util._
import utils._
import unit._
import hazard._
import entity._

class Top extends Module {
  val control = IO(new InstDecodeControl)
  val data    = IO(new InstDecodeData)

  val hazard_unit = Module(new HazardUnit)

  // ========== Instruction Fetch Unit ============
  val inst_fetch_unit = Module(new InstFetchUnit)
  inst_fetch_unit.inst_fetch_hazard <> hazard_unit.inst_fetch_hazard
  inst_fetch_unit.npc := inst_fetch_unit.inst_fetch_data.snpc

  // ========== Instruction Decode Unit ============
  val inst_decode_unit = Module(new InstDecodeUnit)
  inst_decode_unit.inst_decode_hazard <> hazard_unit.inst_decode_hazard
  inst_decode_unit.inst_fetch_data <> inst_fetch_unit.inst_fetch_data

  // ========== Output Test ==========
  control <> inst_decode_unit.inst_decode_control
  data <> inst_decode_unit.inst_fetch_data
}

import chisel3._
import chisel3.util._
import utils._
import unit._
import hazard._
import entity._

class Top extends Module {
  // ========== Units ==========
  val hazard_unit      = Module(new HazardUnit)
  val inst_fetch_unit  = Module(new InstFetchUnit)
  val inst_decode_unit = Module(new InstDecodeUnit)
  val execute_unit     = Module(new ExecuteUnit)
  val load_store_unit  = Module(new LoadStoreUnit)
  val write_back_unit  = Module(new WriteBackUnit)

  // ========== Instruction Fetch Unit ============
  inst_fetch_unit.inst_fetch_hazard <> hazard_unit.inst_fetch_hazard
  inst_fetch_unit.npc               := Mux(execute_unit.jump_ctl, execute_unit.dnpc, inst_fetch_unit.inst_fetch_data.snpc)

  // ========== Instruction Decode Unit ============
  inst_decode_unit.inst_decode_hazard <> hazard_unit.inst_decode_hazard
  inst_decode_unit.inst_fetch_data    <> inst_fetch_unit.inst_fetch_data
  inst_decode_unit.write_back_data    <> write_back_unit.write_back_data
  inst_decode_unit.execute_forward    <> execute_unit.execute_forward
  inst_decode_unit.load_store_forward <> load_store_unit.load_store_forward
  inst_decode_unit.write_back_forward <> write_back_unit.write_back_forward

  // ========== Execute Unit ==========
  execute_unit.inst_decode_data    <> inst_decode_unit.inst_decode_data
  execute_unit.inst_decode_control <> inst_decode_unit.inst_decode_control
  execute_unit.execute_hazard      <> hazard_unit.execute_hazard

  // ========== Load Store Unit ==========
  load_store_unit.execute_data      <> execute_unit.execute_data
  load_store_unit.execute_control   <> execute_unit.execute_control
  load_store_unit.load_store_hazard <> hazard_unit.load_store_hazard

  // ========== Write Back Unit ==========
  write_back_unit.load_store_data    <> load_store_unit.load_store_data
  write_back_unit.load_store_control <> load_store_unit.load_store_control
  write_back_unit.write_back_hazard  <> hazard_unit.write_back_hazard

  // ========== Output Test ==========
}

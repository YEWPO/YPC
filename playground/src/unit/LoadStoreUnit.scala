package unit

import chisel3._
import entity._
import utils._
import control._

class LoadStoreUnit extends Module {
  val execute_data       = IO(Flipped(new ExecuteData))
  val execute_control    = IO(Flipped(new ExecuteControl))
  val load_store_data    = IO(new LoadStoreData)
  val load_store_control = IO(new LoadStoreControl)
  val load_store_hazard  = IO(new LoadStoreHazard)
  val load_store_forward = IO(new LoadStoreForward)

  val data_mem = Module(new DataMem)

  // data registers
  val snpc    = RegNext(execute_data.snpc, CommonMacro.PC_RESET_VAL)
  val pc      = RegNext(execute_data.pc, CommonMacro.PC_RESET_VAL)
  val dnpc    = RegNext(execute_data.dnpc, CommonMacro.PC_RESET_VAL)
  val inst    = RegNext(execute_data.inst, CommonMacro.INST_RESET_VAL)
  val rd      = RegNext(execute_data.rd, 0.U)
  val src2    = RegNext(execute_data.src2, 0.U)
  val exe_out = RegNext(execute_data.exe_out, 0.U)

  // control registers
  val mem_ctl    = RegNext(execute_control.mem_ctl, ControlMacro.MEM_CTL_DEFAULT)
  val wb_ctl     = RegNext(execute_control.wb_ctl, ControlMacro.WB_CTL_DEFAULT)
  val reg_w_en   = RegNext(execute_control.reg_w_en, ControlMacro.REG_W_DISABLE)
  val invalid_op = RegNext(execute_control.invalid_op, ControlMacro.INVALID_OP_NO)
  val ebreak_op  = RegNext(execute_control.ebreak_op, ControlMacro.EBREAK_OP_NO)

  // memory
  data_mem.io.addr    := exe_out
  data_mem.io.w_data  := src2
  data_mem.io.mem_ctl := mem_ctl

  // data part
  load_store_data.snpc    := snpc
  load_store_data.pc      := pc
  load_store_data.dnpc    := dnpc
  load_store_data.inst    := inst
  load_store_data.rd      := rd
  load_store_data.mem_out := data_mem.io.r_data
  load_store_data.exe_out := exe_out

  // control part
  load_store_control.wb_ctl     := wb_ctl
  load_store_control.reg_w_en   := reg_w_en
  load_store_control.ebreak_op  := ebreak_op
  load_store_control.invalid_op := invalid_op

  // forward part
  load_store_forward.snpc    := snpc
  load_store_forward.mem_out := data_mem.io.r_data
  load_store_forward.exe_out := exe_out

  // hazard part
  load_store_hazard.rd     := rd
  load_store_hazard.rd_tag := reg_w_en
  load_store_hazard.wb_ctl := wb_ctl
}

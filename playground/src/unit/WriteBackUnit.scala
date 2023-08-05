package unit

import chisel3._
import entity._
import control._
import chisel3.util._
import utils._

class WriteBackUnit extends Module {
  val load_store_data    = IO(Flipped(new LoadStoreData))
  val load_store_control = IO(Flipped(new LoadStoreControl))
  val write_back_data    = IO(new WriteBackData)
  val write_back_control = IO(new WriteBackControl)
  val write_back_hazard  = IO(new WriteBackHazard)
  val write_back_forward = IO(new WriteBackForward)

  val other_operation = Module(new OtherOperation)

  // data registers
  val snpc    = RegNext(load_store_data.snpc, CommonMacro.PC_RESET_VAL)
  val pc      = RegNext(load_store_data.pc, CommonMacro.PC_RESET_VAL)
  val inst    = RegNext(load_store_data.inst, CommonMacro.INST_RESET_VAL)
  val rd      = RegNext(load_store_data.rd, 0.U)
  val mem_out = RegNext(load_store_data.mem_out, 0.U)
  val exe_out = RegNext(load_store_data.exe_out, 0.U)

  // control registers
  val wb_ctl     = RegNext(load_store_control.wb_ctl, ControlMacro.WB_CTL_DEFAULT)
  val reg_w_en   = RegNext(load_store_control.reg_w_en, ControlMacro.REG_W_DISABLE)
  val ebreak_op  = RegNext(load_store_control.ebreak_op, ControlMacro.EBREAK_OP_NO)
  val invalid_op = RegNext(load_store_control.invalid_op, ControlMacro.INVALID_OP_NO)

  // other operation
  other_operation.io.ebreak  := load_store_control.ebreak_op
  other_operation.io.invalid := load_store_control.invalid_op

  val wb_map = Seq(
    0.U -> exe_out,
    1.U -> mem_out,
    2.U -> snpc
  )
  val wb_data = MuxLookup(wb_ctl, 0.U(64.W))(wb_map)

  // data part
  write_back_data.rd      := rd
  write_back_data.wb_data := wb_data

  // control part
  write_back_control.reg_w_en := reg_w_en

  // hazard part
  write_back_hazard.rd     := rd
  write_back_hazard.rd_tag := reg_w_en

  // forward part
  write_back_forward.wb_data := wb_data
}

package unit

import chisel3._
import entity._
import utils._

class OutInfoUnit extends Module {
  val out_info = IO(Flipped(new OutInfoData))

  val other_operation = Module(new OtherOperation)
  val debug_part      = Module(new DebugPart)

  val pc         = RegNext(out_info.pc, CommonMacro.PC_RESET_VAL)
  val dnpc       = RegNext(out_info.dnpc, CommonMacro.PC_RESET_VAL)
  val inst       = RegNext(out_info.inst, CommonMacro.INST_RESET_VAL)
  val ebreak_op  = RegNext(out_info.ebreak_op, false.B)
  val invalid_op = RegNext(out_info.invalid_op, false.B)

  val pc_1         = RegNext(pc, CommonMacro.PC_RESET_VAL)
  val dnpc_1       = RegNext(dnpc, CommonMacro.PC_RESET_VAL)
  val inst_1       = RegNext(inst, CommonMacro.INST_RESET_VAL)
  val ebreak_op_1  = RegNext(ebreak_op, false.B)
  val invalid_op_1 = RegNext(invalid_op, false.B)

  other_operation.io.ebreak_op  := ebreak_op_1
  other_operation.io.invalid_op := invalid_op_1
  other_operation.io.pc         := pc_1

  debug_part.io.pc   := pc_1
  debug_part.io.inst := inst_1
  debug_part.io.dnpc := dnpc_1
}

import chisel3._
import chisel3.util._

import register._
import decode._

class Top extends Module {
  val io = IO(new Bundle{
    val inst = Input(UInt(32.W))
    val pc = Output(UInt(64.W))
  })

  val program_counter = Module(new PC)
  val reg_file = Module(new Regs)

  io.pc := program_counter.io.pc
  // reg src id
  reg_file.io.r_rs1 := io.inst(19, 15)
  reg_file.io.r_rs2 := io.inst(24, 20)

  // parse op type
  val parse_optype = Module(new OperationType)
  parse_optype.io.operation := io.inst(6, 0)

  // parse imm
  val parse_imm = Module(new ImmPart)
  parse_imm.io.inst := io.inst
  parse_imm.io.op_type := parse_optype.io.operation_type

  // generate signal for selectors
  val sel_sigs = Module(new DecodeSignal)
  sel_sigs.io.opcode := io.inst(6, 0)
  sel_sigs.io.optype := parse_optype.io.operation_type
  sel_sigs.io.opfunct := io.inst(14, 12)
}

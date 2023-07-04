import chisel3._
import chisel3.util._

import register._
import decode._
import execute._
import memory._

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

  // selector A
  val selector_a = Module(new SelectorA)
  selector_a.io.pc_val := io.pc
  selector_a.io.src1 := reg_file.io.r_data1
  selector_a.io.sel_sig_0 := sel_sigs.io.a_sel_0
  selector_a.io.sel_sig_pc := sel_sigs.io.a_sel_pc

  // selector B
  val selector_b = Module(new SelectorB)
  selector_b.io.imm_val := parse_imm.io.imm_out
  selector_b.io.src2 := reg_file.io.r_data2
  selector_b.io.sel_sig_imm := sel_sigs.io.b_sel_imm

  // Branch selector
  val branch = Module(new Branch)
  branch.io.src1 := reg_file.io.r_data1
  branch.io.src2 := reg_file.io.r_data2
  branch.io.funct := io.inst(14, 12)
  branch.io.branch_en := sel_sigs.io.branch_en

  // ALU
  val alu = Module(new Calculator)
  alu.io.src1 := selector_a.io.sel_out
  alu.io.src2 := selector_b.io.sel_out
  alu.io.funct := io.inst(14, 12)
  alu.io.word_op := sel_sigs.io.word_en
  alu.io.subop_type := sel_sigs.io.op30_en

  // MemOpMask
  val mem_op_mask = Module(new MemOpMask)
  mem_op_mask.io.funct := io.inst(14, 12)
}

import chisel3._
import chisel3.util._

import register._
import decode._
import execute._
import pmem._

class Top extends Module {
  val io = IO(new Bundle{
    val inst = Input(UInt(32.W))
    val pc = Output(UInt(64.W))
  })

  val ebreak = Module(new Ebreak)
  ebreak.io.inst := io.inst

  val program_counter = Module(new PC)
  val reg_file = Module(new Regs)
  val snpc_gen = Module(new Snpc)

  snpc_gen.io.opcode := io.inst(1, 0)
  snpc_gen.io.pc := program_counter.io.pc
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
  selector_a.io.pc_val := program_counter.io.pc
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
  branch.io.funct := Mux(sel_sigs.io.funct_en, io.inst(14, 12), 0.U)
  branch.io.branch_en := sel_sigs.io.branch_en

  // ALU
  val alu = Module(new Calculator)
  alu.io.src1 := selector_a.io.sel_out
  alu.io.src2 := selector_b.io.sel_out
  alu.io.funct := Mux(sel_sigs.io.funct_en, io.inst(14, 12), 0.U)
  alu.io.subop_type := Mux(sel_sigs.io.op30_en, io.inst(30), 0.U)
  alu.io.word_op := sel_sigs.io.word_en

  // MemOpMask
  val mem_op_mask = Module(new MemOpMask)
  mem_op_mask.io.funct := Mux(sel_sigs.io.funct_en, io.inst(14, 12), 0.U)

  // memory part
  val pmem = Module(new Pmem)
  pmem.io.mem_en := sel_sigs.io.mem_en
  pmem.io.w_en := sel_sigs.io.mem_w_en
  pmem.io.signed_en := mem_op_mask.io.signed
  pmem.io.addr := alu.io.res
  pmem.io.w_data := reg_file.io.r_data2
  pmem.io.r_mask := mem_op_mask.io.mask

  // write register
  val sel_reg = Module(new SelectorRegs)
  sel_reg.io.opcode := io.inst(6, 0)
  sel_reg.io.snpc := snpc_gen.io.snpc
  sel_reg.io.alu_out := pmem.io.r_data
  reg_file.io.w_en := sel_sigs.io.reg_en
  reg_file.io.w_rd := io.inst(11, 7)
  reg_file.io.w_data := sel_reg.io.data_out

  // update program_counter
  val sel_pc = Module(new SelectorPC)
  sel_pc.io.opcode := io.inst(6, 0)
  sel_pc.io.branch_out := branch.io.res
  sel_pc.io.snpc := snpc_gen.io.snpc
  sel_pc.io.alu_out := pmem.io.r_data
  program_counter.io.next_pc := sel_pc.io.next_pc

  io.pc := program_counter.io.pc
}

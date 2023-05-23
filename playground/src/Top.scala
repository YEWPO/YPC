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

  val pc = Module(new PC)
  val mem = Module(new Memory)
  val regs_file = Module(new Regs)
  val decoder = Module(new Decoder)

  io.pc := pc.io.pc

  decoder.io.inst := io.inst
  decoder.io.src1 := regs_file.io.r_data1
  decoder.io.src2 := regs_file.io.r_data2
  decoder.io.pc := pc.io.pc

  val alu = Module(new Calculator)

  alu.io.src1 := decoder.io.out_src1
  alu.io.src2 := decoder.io.out_src2
  alu.io.funct := io.inst(14, 12)
  alu.io.subop_type := decoder.io.sub_op
  alu.io.word_op := decoder.io.word_op

  val branch = Module(new Branch)

  branch.io.src1 := regs_file.io.r_data1
  branch.io.src2 := regs_file.io.r_data2
  branch.io.funct := io.inst(14, 12)
  branch.io.branch_en := decoder.io.B_type

  val alu_out = Mux(io.inst(6, 0) === "b1100111".U, Cat(alu.io.res(63, 1), 0.U(1.W)), alu.io.res)

  val snpc = pc.io.pc + 4.U(64.W)
  val dnpc = alu_out

  val snpc_op = (
    (io.inst(6, 0) === "b1101111".U)
    || (io.inst(6, 0) === "b1100111".U)
  )

  val reg_op = decoder.io.S_type

  regs_file.io.w_en := decoder.io.R_type || decoder.io.I_type || decoder.io.U_type || decoder.io.J_type
  regs_file.io.w_data := Mux(
    reg_op,
    mem.io.r_data,
    Mux(snpc_op, snpc, alu_out)
  )
  regs_file.io.w_rd := io.inst(11, 7)
  regs_file.io.r_rs1 := io.inst(19, 15)
  regs_file.io.r_rs2 := io.inst(24, 20)

  val pc_op = decoder.io.J_type || branch.io.res

  pc.io.next_pc := Mux(pc_op, dnpc, snpc)

  mem.io.addr := alu_out
  mem.io.w_data := regs_file.io.r_data2
}

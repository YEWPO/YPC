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

  val funct_op = (
    (io.inst(6, 0) === "b0010011".U)
    || (io.inst(6, 0) === "b0110011".U)
    || (io.inst(6, 0) === "b0011011".U)
    || (io.inst(6, 0) === "b0111011".U)
  )

  alu.io.src1 := decoder.io.out_src1
  alu.io.src2 := decoder.io.out_src2
  alu.io.funct := Mux(funct_op, io.inst(14, 12), 0.U(3.W))
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

  val mem_op = io.inst(6, 0) === "b0000011".U

  regs_file.io.w_en := decoder.io.R_type || decoder.io.I_type || decoder.io.U_type || decoder.io.J_type
  regs_file.io.w_data := Mux(
    mem_op,
    mem.io.r_data,
    Mux(snpc_op, snpc, alu_out)
  )
  regs_file.io.w_rd := io.inst(11, 7)
  regs_file.io.r_rs1 := io.inst(19, 15)
  regs_file.io.r_rs2 := io.inst(24, 20)

  val pc_op = snpc_op || branch.io.res 

  pc.io.next_pc := Mux(pc_op, dnpc, snpc)

  mem.io.addr := alu_out
  mem.io.w_data := regs_file.io.r_data2
  mem.io.op := decoder.io.S_type
  mem.io.len := MuxCase(1.U(32.W), Array (
    (io.inst(13, 12) === "b00".U) -> 1.U(32.W),
    (io.inst(13, 12) === "b01".U) -> 2.U(32.W),
    (io.inst(13, 12) === "b10".U) -> 4.U(32.W),
    (io.inst(13, 12) === "b11".U) -> 8.U(32.W)
    ))
  mem.io.m_en := (
    decoder.io.S_type
    || io.inst(6, 0) === "b0000011".U
  )
  mem.io.funct := io.inst(14, 12)
}

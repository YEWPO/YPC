package decode

import chisel3._

class DecodeSignal extends Module {
  val io = IO(new Bundle {
    val opcode = Input(UInt(7.W))
    val optype = Input(UInt(3.W))
    val opfunct = Input(UInt(3.W))

    val a_sel_0 = Output(Bool())
    val a_sel_pc = Output(Bool())

    val b_sel_imm = Output(Bool())

    val reg_en = Output(Bool())
    val funct_en = Output(Bool())
    val word_en = Output(Bool())
    val mem_en = Output(Bool())
    val mem_w_en = Output(Bool())
    val op30_en = Output(Bool())
    val branch_en = Output(Bool())
  })

  io.a_sel_0 := io.opcode === "b0110111".U
  io.a_sel_pc := (io.opcode === "b0010111".U) || (io.optype === "b011".U)
  io.b_sel_imm := io.optype =/= "b000".U

  // !B, !S
  io.reg_en := (io.optype =/= "b011".U) && (io.optype =/= "b010".U)
  // !J, !U
  io.funct_en := (io.optype =/= "b101".U) && (io.optype =/= "b100".U)
  // word
  io.word_en := (io.opcode === "b0011011".U) || (io.opcode === "b0111011".U)
  // sd, ld
  io.mem_en := (io.opcode === "b0000011".U) || (io.opcode === "b0100011".U)
  // R, I - sra
  io.op30_en := (io.optype === "b000".U) || (io.optype === "b001".U && io.opcode =/= "b0000011".U && io.opfunct === "b101".U)
  // B
  io.branch_en := io.optype === "b011".U
  // S
  io.mem_w_en := io.optype === "b010".U
}

package utils

import chisel3._
import chisel3.util._

/**
  * immediate type macros
  */
object ImmType {
  val IMM_TYPE_R = "b000".U(3.W)
  val IMM_TYPE_I = "b001".U(3.W)
  val IMM_TYPE_S = "b010".U(3.W)
  val IMM_TYPE_B = "b011".U(3.W)
  val IMM_TYPE_U = "b100".U(3.W)
  val IMM_TYPE_J = "b101".U(3.W)
}

class ImmGenIO extends Bundle {
  val in = Input(UInt(25.W))
  val imm_type = Input(UInt(3.W))

  val imm_out = Output(UInt(64.W))
}

class ImmGen extends Module {
  val io = IO(new ImmGenIO())

  def instSlice(high: Int, low: Int): UInt = io.in(high - 7, low - 7)

  /**
   * I,S,B,U,J type immediate needs signed extend
   *
   * +-------------+
   * | inst[31:20] | I-immediate
   * +-------------+
   * +-------------+------------+
   * | inst[31:25] | inst[11:7] | S-immediate
   * +-------------+------------+
   * +----------+---------+-------------+------------+---+
   * | inst[31] | inst[7] | inst[30:25] | inst[11:8] | 0 | B-immediate
   * +----------+---------+-------------+------------+---+
   * +-------------+-----------+
   * | inst[31:12] | 0(12bits) | U-immediate
   * +-------------+-----------+
   * +----------+-------------+----------+-------------+---+
   * | inst[31] | inst[19:12] | inst[20] | inst[30:21] | 0 | J-immediate
   * +----------+-------------+----------+-------------+---+
   */
  val immI = Cat(instSlice(31, 20)).asSInt
  val immS = Cat(instSlice(31, 25), instSlice(11, 7)).asSInt
  val immB = Cat(instSlice(31, 31), instSlice(7, 7), instSlice(30, 25), instSlice(11, 8), 0.U(1.W)).asSInt
  val immU = Cat(instSlice(31, 12), 0.U(12.W)).asSInt
  val immJ = Cat(instSlice(31, 31), instSlice(19, 12), instSlice(20, 20), instSlice(30, 21), 0.U(1.W)).asSInt

  val imm_out_map = Seq(
    ImmType.IMM_TYPE_I -> immI.asUInt,
    ImmType.IMM_TYPE_S -> immS.asUInt,
    ImmType.IMM_TYPE_B -> immB.asUInt,
    ImmType.IMM_TYPE_U -> immU.asUInt,
    ImmType.IMM_TYPE_J -> immJ.asUInt,
  )

  io.imm_out := MuxLookup(io.imm_type, 0.U(64.W))(imm_out_map)
}

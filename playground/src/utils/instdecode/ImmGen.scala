package utils.instdecode

import chisel3._
import chisel3.util._
import control._
import unit._

class ImmGenIO extends Bundle {
  val in       = Input(UInt(25.W))
  val imm_type = Input(UInt(3.W))

  val imm_out = Output(UInt(64.W))
}

class ImmGen extends Module {
  val io = IO(new ImmGenIO)

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
  val immI = instSlice(31, 20)
  val immS = Cat(instSlice(31, 25), instSlice(11, 7))
  val immB =
    Cat(instSlice(31, 31), instSlice(7, 7), instSlice(30, 25), instSlice(11, 8), 0.U(1.W))
  val immU = Cat(instSlice(31, 12), 0.U(12.W))
  val immJ = Cat(
    instSlice(31, 31),
    instSlice(19, 12),
    instSlice(20, 20),
    instSlice(30, 21),
    0.U(1.W)
  )

  val imm_out_map = Seq(
    ControlMacro.IMM_TYPE_I -> CommonMacro.signExtend(immI),
    ControlMacro.IMM_TYPE_S -> CommonMacro.signExtend(immS),
    ControlMacro.IMM_TYPE_B -> CommonMacro.signExtend(immB),
    ControlMacro.IMM_TYPE_U -> CommonMacro.signExtend(immU),
    ControlMacro.IMM_TYPE_J -> CommonMacro.signExtend(immJ)
  )

  io.imm_out := MuxLookup(io.imm_type, 0.U(64.W))(imm_out_map)
}

package decode

import chisel3._
import chisel3.util._

class ImmPart extends Module {
  val io = IO(new Bundle {
    val inst = Input(UInt(32.W))
    val op_type = Input(UInt(3.W))
    val imm_out = Output(UInt(64.W))
  })

  val immI = Cat(
    Fill(32, io.inst(31)),
    Cat(
      Fill(20, io.inst(31)),
      io.inst(31, 20)
    )
  )
  val immS = Cat(
    Fill(32, io.inst(31)),
    Cat(
      Fill(20, io.inst(31)),
      Cat(
        io.inst(31, 25),
        io.inst(11, 7)
      )
    )
  )
  val immB = Cat(
    Fill(32, io.inst(31)),
    Cat(
      Fill(20, io.inst(31)),
      Cat(
        io.inst(7),
        Cat(
          io.inst(30, 25),
          Cat(
            io.inst(11, 8),
            0.U(1.W)
          )
        )
      )
    )
  )
  val immU = Cat(
    Fill(32, io.inst(31)),
    Cat(
      Cat(
        io.inst(31, 12),
        0.U(12.W)
      )
    )
  )
  val immJ = Cat(
    Fill(32, io.inst(31)),
    Cat(
      Fill(12, io.inst(31)),
      Cat(
        io.inst(19, 12),
        Cat(
          io.inst(20),
          Cat(
            io.inst(30, 21),
            0.U(1.W)
          )
        )
      )
    )
  )

  io.imm_out := MuxCase(0.U(64.W), Array (
    (io.op_type === "b001".U) -> immI,
    (io.op_type === "b010".U) -> immS,
    (io.op_type === "b011".U) -> immB,
    (io.op_type === "b100".U) -> immU,
    (io.op_type === "b101".U) -> immJ
    ))
}

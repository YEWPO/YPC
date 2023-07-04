package pmem

import chisel3._
import chisel3.util._

class MemOpMask extends Module {
  val io = IO(new Bundle {
    val funct = Input(UInt(3.W))

    val signed = Output(Bool())
    val mask = Output(UInt(64.W))
  })

  io.mask := MuxCase(0.U(64.W), Array(
    (io.funct(1, 0) === "b00".U) -> "h0000_0000_0000_00ff".U,
    (io.funct(1, 0) === "b01".U) -> "h0000_0000_0000_ffff".U,
    (io.funct(1, 0) === "b10".U) -> "h0000_0000_ffff_ffff".U,
    (io.funct(1, 0) === "b11".U) -> "hffff_ffff_ffff_ffff".U
    ))

  io.signed := io.funct(2) === "b0".U
}

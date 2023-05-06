package decode

import chisel3._
import chisel3.util._

class OperationType extends Module {
  val io = IO(new Bundle {
    val operation = Input(UInt(7.W))
    val operation_type = Output(UInt(3.W))
  })


  /*
   * 000 R-type
   * 001 I-type
   * 010 S-type
   * 011 B-type
   * 100 U-type
   * 101 J-type
   * 110 N-type
   * 111 undefined type
   */
  io.operation_type := MuxCase("b111".U, Array(
    (io.operation === "b0110111".U) -> "b100".U,
    (io.operation === "b0010111".U) -> "b100".U,
    (io.operation === "b0010011".U) -> "b001".U,
    (io.operation === "b0010011".U) -> "b001".U,
    (io.operation === "b0010011".U) -> "b001".U,
    (io.operation === "b0010011".U) -> "b001".U,
    (io.operation === "b0010011".U) -> "b001".U,
    (io.operation === "b0010011".U) -> "b001".U,
    (io.operation === "b0010011".U) -> "b001".U,
    (io.operation === "b0010011".U) -> "b001".U,
    (io.operation === "b0010011".U) -> "b001".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0011011".U) -> "b001".U,
    (io.operation === "b0011011".U) -> "b001".U,
    (io.operation === "b0011011".U) -> "b001".U,
    (io.operation === "b0011011".U) -> "b001".U,
    (io.operation === "b0111011".U) -> "b000".U,
    (io.operation === "b0111011".U) -> "b000".U,
    (io.operation === "b0111011".U) -> "b000".U,
    (io.operation === "b0111011".U) -> "b000".U,
    (io.operation === "b0111011".U) -> "b000".U,
    (io.operation === "b1101111".U) -> "b101".U,
    (io.operation === "b1100111".U) -> "b001".U,
    (io.operation === "b1100011".U) -> "b011".U,
    (io.operation === "b1100011".U) -> "b011".U,
    (io.operation === "b1100011".U) -> "b011".U,
    (io.operation === "b1100011".U) -> "b011".U,
    (io.operation === "b1100011".U) -> "b011".U,
    (io.operation === "b1100011".U) -> "b011".U,
    (io.operation === "b0000011".U) -> "b001".U,
    (io.operation === "b0000011".U) -> "b001".U,
    (io.operation === "b0000011".U) -> "b001".U,
    (io.operation === "b0000011".U) -> "b001".U,
    (io.operation === "b0000011".U) -> "b001".U,
    (io.operation === "b0000011".U) -> "b001".U,
    (io.operation === "b0000011".U) -> "b001".U,
    (io.operation === "b0100011".U) -> "b010".U,
    (io.operation === "b0100011".U) -> "b010".U,
    (io.operation === "b0100011".U) -> "b010".U,
    (io.operation === "b0100011".U) -> "b010".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0111011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0110011".U) -> "b000".U,
    (io.operation === "b0111011".U) -> "b000".U,
    (io.operation === "b0111011".U) -> "b000".U,
    (io.operation === "b0111011".U) -> "b000".U,
    (io.operation === "b0111011".U) -> "b000".U,
    (io.operation === "b1110011".U) -> "b110".U,
    ))
}

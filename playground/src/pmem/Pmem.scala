package pmem

import chisel3._
import chisel3.util.HasBlackBoxResource

class Pmem extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val mem_en = Input(Bool())
    val w_en = Input(Bool())
    val signed_en = Input(Bool())
    val addr = Input(UInt(64.W))
    val w_data = Input(UInt(64.W))
    val r_mask = Input(UInt(64.W))
    val r_data = Output(UInt(64.W))
  })

  addResource("Pmem.v")
}

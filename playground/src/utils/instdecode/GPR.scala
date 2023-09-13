package utils.instdecode

import chisel3._

/**
  * rs1: resource addr 1
  * rs2: resource addr 2
  *
  * rd: destination addr
  *
  * w_en: write enable signal
  * w_data: write data
  *
  * r_data1: the read data 1
  * r_data2: the read data 2
  */
class GPRIO extends Bundle {
  val rs1 = Input(UInt(5.W))
  val rs2 = Input(UInt(5.W))
  val rd  = Input(UInt(5.W))

  val w_en   = Input(Bool())
  val w_data = Input(UInt(64.W))

  val r_data1 = Output(UInt(64.W))
  val r_data2 = Output(UInt(64.W))
}

class GPR extends Module {
  val io = IO(new GPRIO)

  val reg_files = RegInit(VecInit(Seq.fill(32)(0.U(64.W))))

  // write the register only if the w_en signal is enable and rd addr is not zero
  reg_files(io.rd) := Mux(io.rd.orR && io.w_en, io.w_data, reg_files(io.rd))

  // out the data needed to read
  io.r_data1 := reg_files(io.rs1)
  io.r_data2 := reg_files(io.rs2)

  // deliver registers info to the verilator
  val regs_info = Module(new GPRInfo())
  regs_info.io.inbits := reg_files.asUInt
}

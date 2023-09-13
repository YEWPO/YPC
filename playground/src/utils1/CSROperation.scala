package utils

import chisel3._
import chisel3.util._

object CSROperation {
  val RW = "b01".U
  val RS = "b10".U
  val RC = "b11".U
}

class CSROperationIO extends Bundle {
  val src      = Input(UInt(64.W))
  val csr_data = Input(UInt(64.W))

  val csr_op_ctl = Input(UInt(2.W))

  val csr_op_out = Output(UInt(64.W))
}

class CSROperation extends Module {
  val io = IO(new CSROperationIO)

  val csr_op_map = Seq(
    CSROperation.RW -> io.src,
    CSROperation.RS -> (io.csr_data | io.src),
    CSROperation.RC -> (io.csr_data & ~io.src)
  )

  io.csr_op_out := MuxLookup(io.csr_op_ctl, 0.U(64.W))(csr_op_map)
}

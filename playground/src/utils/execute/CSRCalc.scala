package utils.execute

import chisel3._
import chisel3.util._

object CSRCalc {
  val RW = "b01".U
  val RS = "b10".U
  val RC = "b11".U
}

class CSRCalcIO extends Bundle {
  val src      = Input(UInt(64.W))
  val csr_data = Input(UInt(64.W))

  val csr_op_ctl = Input(UInt(2.W))

  val csr_op_out = Output(UInt(64.W))
}

class CSRCalc extends Module {
  val io = IO(new CSRCalcIO)

  val csr_op_map = Seq(
    CSRCalc.RW -> io.src,
    CSRCalc.RS -> (io.csr_data | io.src),
    CSRCalc.RC -> (io.csr_data & ~io.src)
  )

  io.csr_op_out := MuxLookup(io.csr_op_ctl, 0.U(64.W))(csr_op_map)
}

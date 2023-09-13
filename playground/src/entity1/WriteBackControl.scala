package entity

import chisel3._

class WriteBackControl extends Bundle {
  val reg_w_en = Output(Bool())
}

class WriteBackCSRControl extends Bundle {
  val csr_w_en = Output(Bool())
}

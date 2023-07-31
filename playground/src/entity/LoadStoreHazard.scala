package entity

import chisel3._

class LoadStoreHazard extends Bundle {
  val wb_ctl = Output(UInt(2.W))
  val rd     = Output(UInt(5.W))
}

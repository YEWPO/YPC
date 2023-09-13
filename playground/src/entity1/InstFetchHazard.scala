package entity

import chisel3._

class InstFetchHazard extends Bundle {
  val enable = Input(Bool())
}

package bundles.writeback

import chisel3._

class WB2RegBundle extends Bundle {
  val wb_data  = UInt(64.W)
  val rd       = UInt(5.W)
  val reg_w_en = Bool()
}

package bundles

import chisel3._

class StageControlBundle extends Bundle {
  val enable = Bool()
  val reset  = Bool()
}

package utils

import chisel3._
import bundles._

class StageRegIO[+T <: Data](bundle: T) extends Bundle {
  val in      = Input(bundle)
  val out     = Output(bundle)
  val control = Input(new StageControlBundle)
}

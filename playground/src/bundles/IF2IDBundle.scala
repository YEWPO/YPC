package bundles

import chisel3._

class IF2IDDataBundle extends Bundle {
  val inst = UInt(32.W)
  val pc   = UInt(64.W)
  val snpc = UInt(64.W)
}

class IF2IDControlBundle extends Bundle {}

class IF2IDBundle extends Bundle {
  val data    = new IF2IDDataBundle
  val control = new IF2IDControlBundle
}

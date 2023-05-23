import  chisel3._

import register._
import decode._
import execute._
import memory._

class Top extends Module {
  val io = IO(new Bundle{
    val inst = Input(UInt(32.W))
    val pc = Output(UInt(64.W))
  })
}

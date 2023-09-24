import chisel3._
import chisel3.experimental.BundleLiterals._
import stages._

class Top extends Module {
  /* ========== Module ========== */
  val ifu = Module(new IFU)
  val idu = Module(new IDU)
  val exu = Module(new EXU)
  val lsu = Module(new LSU)
  val wbu = Module(new WBU)

  /* ========== Register ========== */

  /* ========== Sequential Circuit ========== */

  /* ========== Combinational Circuit ========== */
}

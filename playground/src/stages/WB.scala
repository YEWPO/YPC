package stages

import chisel3._
import chisel3.util._
import bundles._
import macros._

class WB extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new Bundle {
    val in = Input(new Bundle {
      val data    = new LS2WBDataBundle
      val control = new LS2WBControlBundle
    })
    val out = Output(new Bundle {
      val wb_data  = UInt(64.W)
      val rd       = UInt(5.W)
      val reg_w_en = Bool()
    })
  })

  /* ========== Combinational Circuit ========== */
  io.out.wb_data := MuxLookup(io.in.control.wb_ctl, 0.U(64.W))(
    Seq(
      ControlMacros.WB_CTL_ALU  -> io.in.data.exe_out,
      ControlMacros.WB_CTL_MEM  -> io.in.data.mem_out,
      ControlMacros.WB_CTL_SNPC -> io.in.data.snpc
    )
  )
  io.out.rd       := io.in.data.rd
  io.out.reg_w_en := io.in.control.reg_w_en
}

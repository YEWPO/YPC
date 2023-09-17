package utils.instdecode

import chisel3._
import chisel3.util._
import macros._
import bundles.instdecode._

class GPRForwardIO extends Bundle {
  val data1 = Input(UInt(64.W))
  val data2 = Input(UInt(64.W))

  val forward = Input(new IDForwardBundle)

  val id_f_ctl = Input(new IDHazardControlBundle)

  val src1 = Output(UInt(64.W))
  val src2 = Output(UInt(64.W))
}

class GPRForward extends Module {
  val io = IO(new GPRForwardIO)

  val forward_map = Seq(
    HazardMacros.F_CTL_EXE_E   -> io.forward.exe_E,
    HazardMacros.F_CTL_SNPC_E  -> io.forward.snpc_E,
    HazardMacros.F_CTL_EXE_M   -> io.forward.exe_M,
    HazardMacros.F_CTL_MEM_M   -> io.forward.mem_M,
    HazardMacros.F_CTL_SNPC_M  -> io.forward.snpc_M,
    HazardMacros.F_CTL_WB_DATA -> io.forward.wb_data
  )

  io.src1 := MuxLookup(io.id_f_ctl.fa_ctl, io.data1)(forward_map)
  io.src2 := MuxLookup(io.id_f_ctl.fb_ctl, io.data2)(forward_map)
}

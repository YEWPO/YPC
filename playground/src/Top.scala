import chisel3._
import stages._
import utils.sram._

class Top extends Module {
  /* ========== Module ========== */
  val ifu          = Module(new IFU)
  val idu          = Module(new IDU)
  val exu          = Module(new EXU)
  val lsu          = Module(new LSU)
  val wbu          = Module(new WBU)
  val sram_arbiter = Module(new SRAMArbiter)
  val sram         = Module(new SRAM)

  /* ========== Combinational Circuit ========== */
  sram.io <> sram_arbiter.io.out

  sram_arbiter.io.ifu.ar <> ifu.io.ar
  sram_arbiter.io.ifu.r  <> ifu.io.r

  sram_arbiter.io.lsu.ar <> lsu.io.ar
  sram_arbiter.io.lsu.r  <> lsu.io.r
  sram_arbiter.io.lsu.aw <> lsu.io.aw
  sram_arbiter.io.lsu.w  <> lsu.io.w
  sram_arbiter.io.lsu.b  <> lsu.io.b

  ifu.io.if2id <> idu.io.if2id
  idu.io.id2ex <> exu.io.id2ex
  exu.io.ex2ls <> lsu.io.ex2ls
  lsu.io.ls2wb <> wbu.io.ls2wb

  ifu.io.in.cause    := wbu.io.out.cause
  ifu.io.in.tvec     := idu.io.out.tvec
  ifu.io.in.dnpc     := exu.io.out.dnpc
  ifu.io.in.jump_ctl := exu.io.out.jump_ctl

  idu.io.in.gpr_fw_info.rd_E    := exu.io.out.state_info.rd
  idu.io.in.gpr_fw_info.rd_M    := lsu.io.out.state_info.rd
  idu.io.in.gpr_fw_info.rd_W    := wbu.io.out.state_info.rd
  idu.io.in.gpr_fw_info.exu_out := exu.io.out.state_info.reg_w_data
  idu.io.in.gpr_fw_info.lsu_out := lsu.io.out.state_info.reg_w_data
  idu.io.in.gpr_fw_info.wbu_out := wbu.io.out.state_info.reg_w_data
  idu.io.in.csr_fw_info.addr_E  := exu.io.out.state_info.csr_w_addr
  idu.io.in.csr_fw_info.addr_M  := lsu.io.out.state_info.csr_w_addr
  idu.io.in.csr_fw_info.addr_W  := wbu.io.out.state_info.csr_w_addr
  idu.io.in.csr_fw_info.data_E  := exu.io.out.state_info.csr_w_data
  idu.io.in.csr_fw_info.data_M  := lsu.io.out.state_info.csr_w_data
  idu.io.in.csr_fw_info.data_W  := wbu.io.out.state_info.csr_w_data
  idu.io.in.mem_r_op_E          := exu.io.out.state_info.mem_r_op
  idu.io.in.mem_r_op_M          := lsu.io.out.state_info.mem_r_op
  idu.io.in.wb_data.rd          := wbu.io.out.wb_data.rd
  idu.io.in.wb_data.reg_w_en    := wbu.io.out.wb_data.reg_w_en
  idu.io.in.wb_data.reg_w_data  := wbu.io.out.wb_data.reg_w_data
  idu.io.in.wb_data.csr_w_addr  := wbu.io.out.wb_data.csr_w_addr
  idu.io.in.wb_data.csr_w_en    := wbu.io.out.wb_data.csr_w_en
  idu.io.in.wb_data.csr_w_data  := wbu.io.out.wb_data.csr_w_data
  idu.io.in.cause               := wbu.io.out.cause
  idu.io.in.epc                 := wbu.io.out.epc
  idu.io.in.jump_ctl            := exu.io.out.jump_ctl

  exu.io.in.tvec  := idu.io.out.tvec
  exu.io.in.cause := wbu.io.out.cause

  lsu.io.in.cause := wbu.io.out.cause

  wbu.io.in.tvec := idu.io.out.tvec
}

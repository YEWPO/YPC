package stages

import chisel3._
import bundles._
import bundles.execute._
import utils.execute._
import macros._
import chisel3.util._

class EXUIO extends Bundle {
  val in = Input(new Bundle {
    val data    = new ID2EXDataBundle
    val control = new ID2EXControlBundle
  })
  val out = Output(new Bundle {
    val data       = new EX2LSDataBundle
    val control    = new EX2LSControlBundle
    val hazard     = new EXHazardDataBundle
    val csr_hazard = new EXCSRHazardDataBundle
    val jump_ctl   = Bool()
    val dnpc       = UInt(64.W)
  })
}

class EXU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new EXUIO)

  /* ========== Module ========== */
  val alu      = Module(new AlgLog)
  val mul      = Module(new MulDiv)
  val csr_calc = Module(new CSRCalc)

  /* ========== Wire ========== */
  val w_jump_ctl = (io.in.control.jump_op & Cat(alu.io.alu_out(0), 1.U(1.W))).orR
  val w_dnpc     = Mux(io.in.control.dnpc_ctl, io.in.data.src1, io.in.data.pc) + io.in.data.imm

  /* ========== Combinational Circuit ========== */
  alu.io.src1 := Mux(
    io.in.control.csr_r_en,
    io.in.data.csr_data,
    Mux(io.in.control.a_ctl, io.in.data.pc, io.in.data.src1)
  )
  alu.io.src2            := Mux(io.in.control.b_ctl, io.in.data.src2, io.in.data.imm)
  alu.io.alu_ctl         := io.in.control.alu_ctl
  mul.io.src1            := io.in.data.src1
  mul.io.src2            := io.in.data.src2
  mul.io.mul_ctl         := io.in.control.mul_ctl
  csr_calc.io.csr_data   := io.in.data.csr_data
  csr_calc.io.src        := Mux(io.in.control.csr_src_ctl, io.in.data.csr_uimm, io.in.data.src1)
  csr_calc.io.csr_op_ctl := io.in.control.csr_op_ctl

  io.out.jump_ctl           := w_jump_ctl
  io.out.dnpc               := w_dnpc
  io.out.data.dnpc          := Mux(w_jump_ctl, w_dnpc, io.in.data.snpc)
  io.out.data.snpc          := io.in.data.snpc
  io.out.data.pc            := io.in.data.pc
  io.out.data.inst          := io.in.data.inst
  io.out.data.rd            := io.in.data.rd
  io.out.data.src2          := io.in.data.src2
  io.out.data.exe_out       := Mux(io.in.control.exe_out_ctl, mul.io.mul_out, alu.io.alu_out)
  io.out.control.mem_ctl    := io.in.control.mem_ctl
  io.out.control.wb_ctl     := io.in.control.wb_ctl
  io.out.control.reg_w_en   := io.in.control.reg_w_en
  io.out.control.ebreak_op  := io.in.control.ebreak_op
  io.out.control.invalid_op := io.in.control.invalid_op

  io.out.data.csr_w_addr  := io.in.data.csr_w_addr
  io.out.data.csr_w_data  := csr_calc.io.csr_op_out
  io.out.control.csr_w_en := io.in.control.csr_w_en

  io.out.hazard.jump_sig           := w_jump_ctl
  io.out.hazard.wb_ctl             := io.in.control.wb_ctl
  io.out.hazard.rd                 := io.in.data.rd
  io.out.hazard.rd_tag             := io.in.control.reg_w_en
  io.out.csr_hazard.csr_w_addr     := io.in.data.csr_w_addr
  io.out.csr_hazard.csr_w_addr_tag := io.in.control.csr_w_en
}

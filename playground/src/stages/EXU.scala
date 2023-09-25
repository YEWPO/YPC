package stages

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import bundles._
import utils.execute._
import macros._

class EXUIO extends Bundle {
  val id2ex = Flipped(Decoupled(new ID2EXBundle))
  val ex2ls = Decoupled(new EX2LSBundle)
  val out = Output(new Bundle {
    val jump_ctl = Bool()
    val dnpc     = UInt(64.W)
    val state_info = Output(new Bundle {
      val rd         = UInt(5.W)
      val reg_w_data = UInt(64.W)
      val csr_w_addr = UInt(12.W)
      val csr_w_data = UInt(64.W)
      val mem_r_op   = UInt(64.W)
    })
  })
}

class EXU extends Module {
  /* ========== Input and Output ========== */
  val io = IO(new EXUIO)

  /* ========== Module ========== */
  val alu      = Module(new AlgLog)
  val mul      = Module(new MulDiv)
  val csr_calc = Module(new CSRCalc)

  /* ========== Register ========== */
  val r_valid = RegInit(false.B)
  val r_ex2ls = RegInit(EX2LSBundle.ex2ls_rst_val)

  val r_dnpc = RegInit(CommonMacros.PC_RESET_VAL)

  /* ========== Wire ========== */
  val ready_next    = io.id2ex.valid && (!io.ex2ls.valid || io.ex2ls.ready)
  val valid_enable  = io.id2ex.valid && (!io.ex2ls.valid || io.ex2ls.ready)
  val id2ex_data    = Wire(new ID2EXBundle)
  val valid_next    = r_valid && !io.ex2ls.fire
  val valid_current = io.id2ex.valid && (r_dnpc === io.id2ex.bits.data.pc)

  val jump_ctl    = (id2ex_data.control.jump_op & Cat(alu.io.alu_out(0), 1.U(1.W))).orR
  val dnpc_0      = Mux(id2ex_data.control.dnpc_ctl, id2ex_data.data.src1, id2ex_data.data.pc) + id2ex_data.data.imm
  val dnpc_1      = Mux(jump_ctl, dnpc_0, id2ex_data.data.dnpc)
  val dnpc_enable = (r_dnpc === id2ex_data.data.pc) && ready_next
  val exu_out     = Mux(id2ex_data.control.exe_out_ctl, mul.io.mul_out, alu.io.alu_out)

  /* ========== Sequential Circuit ========== */
  r_valid := Mux(valid_enable, valid_current, valid_next)

  r_dnpc := Mux(dnpc_enable, dnpc_1, r_dnpc)

  r_ex2ls.data.dnpc          := dnpc_1
  r_ex2ls.data.pc            := id2ex_data.data.pc
  r_ex2ls.data.inst          := id2ex_data.data.inst
  r_ex2ls.data.rd            := id2ex_data.data.rd
  r_ex2ls.data.src2          := id2ex_data.data.src2
  r_ex2ls.data.exu_out       := exu_out
  r_ex2ls.control.mem_ctl    := id2ex_data.control.mem_ctl
  r_ex2ls.control.reg_w_en   := id2ex_data.control.reg_w_en
  r_ex2ls.control.ebreak_op  := id2ex_data.control.ebreak_op
  r_ex2ls.control.invalid_op := id2ex_data.control.invalid_op

  r_ex2ls.data.csr_w_addr  := id2ex_data.data.csr_w_addr
  r_ex2ls.data.csr_w_data  := csr_calc.io.csr_op_out
  r_ex2ls.control.csr_w_en := id2ex_data.control.csr_w_en

  /* ========== Combinational Circuit ========== */
  io.id2ex.ready := ready_next
  io.ex2ls.valid := r_valid

  id2ex_data := Mux(valid_current, io.id2ex.bits, ID2EXBundle.id2ex_rst_val)

  io.ex2ls.bits := r_ex2ls

  alu.io.src1 := Mux(
    id2ex_data.control.csr_r_en,
    id2ex_data.data.csr_data,
    MuxLookup(id2ex_data.control.a_ctl, 0.U(64.W))(
      Seq(
        ControlMacros.A_CTL_SRC1 -> id2ex_data.data.src1,
        ControlMacros.A_CTL_PC   -> id2ex_data.data.pc,
        ControlMacros.A_CTL_SNPC -> id2ex_data.data.snpc
      )
    )
  )
  alu.io.src2            := Mux(id2ex_data.control.b_ctl, id2ex_data.data.src2, id2ex_data.data.imm)
  alu.io.alu_ctl         := id2ex_data.control.alu_ctl
  mul.io.src1            := id2ex_data.data.src1
  mul.io.src2            := id2ex_data.data.src2
  mul.io.mul_ctl         := id2ex_data.control.mul_ctl
  csr_calc.io.csr_data   := id2ex_data.data.csr_data
  csr_calc.io.src        := Mux(id2ex_data.control.csr_src_ctl, id2ex_data.data.csr_uimm, id2ex_data.data.src1)
  csr_calc.io.csr_op_ctl := id2ex_data.control.csr_op_ctl

  io.out.jump_ctl := jump_ctl
  io.out.dnpc     := dnpc_1

  io.out.state_info.rd         := Mux(id2ex_data.control.reg_w_en, id2ex_data.data.rd, 0.U(5.W))
  io.out.state_info.reg_w_data := exu_out
  io.out.state_info.csr_w_addr := Mux(id2ex_data.control.csr_w_en, id2ex_data.data.csr_w_addr, 0.U(12.W))
  io.out.state_info.csr_w_data := csr_calc.io.csr_op_out
  io.out.state_info.mem_r_op   := id2ex_data.control.mem_ctl(3)
}

package stages

import chisel3._
import chisel3.util._
import chisel3.experimental.BundleLiterals._
import bundles._
import bundles.execute._
import utils.execute._
import macros._

class EXUIO extends Bundle {
  val id2ex = Flipped(Decoupled(new ID2EXBundle))
  val ex2ls = Decoupled(new EX2LSBundle)
  val out = Output(new Bundle {
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

  /* ========== Parameter ========== */
  val id2ex_rst_val = (new ID2EXBundle).Lit(
    _.data -> (new ID2EXDataBundle).Lit(
      _.pc         -> CommonMacros.PC_RESET_VAL,
      _.snpc       -> CommonMacros.PC_RESET_VAL,
      _.dnpc       -> CommonMacros.PC_RESET_VAL,
      _.inst       -> CommonMacros.INST_RESET_VAL,
      _.rd         -> 0.U,
      _.src1       -> 0.U,
      _.src2       -> 0.U,
      _.imm        -> 0.U,
      _.csr_data   -> 0.U,
      _.csr_uimm   -> 0.U,
      _.csr_w_addr -> 0.U
    ),
    _.control -> (new ID2EXControlBundle).Lit(
      _.a_ctl       -> ControlMacros.A_CTL_DEFAULT,
      _.b_ctl       -> ControlMacros.B_CTL_DEFAULT,
      _.dnpc_ctl    -> ControlMacros.DNPC_CTL_DEFAULT,
      _.alu_ctl     -> ControlMacros.ALU_CTL_DEFAULT,
      _.mul_ctl     -> ControlMacros.MUL_CTL_DEFAULT,
      _.exe_out_ctl -> ControlMacros.EXE_OUT_DEFAULT,
      _.jump_op     -> ControlMacros.JUMP_OP_DEFAULT,
      _.mem_ctl     -> ControlMacros.MEM_CTL_DEFAULT,
      _.reg_w_en    -> ControlMacros.REG_W_DISABLE,
      _.ebreak_op   -> ControlMacros.EBREAK_OP_NO,
      _.invalid_op  -> ControlMacros.INVALID_OP_NO,
      _.csr_r_en    -> false.B,
      _.csr_w_en    -> false.B,
      _.csr_src_ctl -> false.B,
      _.csr_op_ctl  -> 0.U
    )
  )
  val ex2ls_rst_val = (new EX2LSBundle).Lit(
    _.data -> (new EX2LSDataBundle).Lit(
      _.pc         -> CommonMacros.PC_RESET_VAL,
      _.dnpc       -> CommonMacros.PC_RESET_VAL,
      _.inst       -> CommonMacros.INST_RESET_VAL,
      _.rd         -> 0.U,
      _.src2       -> 0.U,
      _.exu_out    -> 0.U,
      _.csr_w_data -> 0.U,
      _.csr_w_addr -> 0.U
    ),
    _.control -> (new EX2LSControlBundle).Lit(
      _.mem_ctl    -> ControlMacros.MEM_CTL_DEFAULT,
      _.reg_w_en   -> ControlMacros.REG_W_DISABLE,
      _.invalid_op -> ControlMacros.INVALID_OP_NO,
      _.ebreak_op  -> ControlMacros.EBREAK_OP_NO,
      _.csr_w_en   -> false.B
    )
  )

  /* ========== Register ========== */
  val r_valid = RegInit(false.B)
  val r_ex2ls = RegInit(ex2ls_rst_val)

  val r_dnpc = RegInit(CommonMacros.PC_RESET_VAL)

  /* ========== Wire ========== */
  val ready_next    = io.id2ex.valid && !io.id2ex.ready && (!io.ex2ls.valid || io.ex2ls.ready)
  val valid_enable  = io.id2ex.valid && !io.id2ex.ready && (!io.ex2ls.valid || io.ex2ls.ready)
  val id2ex_data    = Wire(new ID2EXBundle)
  val valid_next    = r_valid && !io.ex2ls.fire
  val valid_current = io.id2ex.valid && (r_dnpc === io.id2ex.bits.data.pc)

  val jump_ctl    = (id2ex_data.control.jump_op & Cat(alu.io.alu_out(0), 1.U(1.W))).orR
  val dnpc_0      = Mux(id2ex_data.control.dnpc_ctl, id2ex_data.data.src1, id2ex_data.data.pc) + id2ex_data.data.imm
  val dnpc_1      = Mux(jump_ctl, dnpc_0, id2ex_data.data.dnpc)
  val dnpc_enable = (r_dnpc === id2ex_data.data.pc) && ready_next

  /* ========== Sequential Circuit ========== */
  r_valid := Mux(valid_enable, valid_current, valid_next)

  r_dnpc := Mux(dnpc_enable, dnpc_1, r_dnpc)

  r_ex2ls.data.dnpc          := dnpc_1
  r_ex2ls.data.pc            := id2ex_data.data.pc
  r_ex2ls.data.inst          := id2ex_data.data.inst
  r_ex2ls.data.rd            := id2ex_data.data.rd
  r_ex2ls.data.src2          := id2ex_data.data.src2
  r_ex2ls.data.exu_out       := Mux(id2ex_data.control.exe_out_ctl, mul.io.mul_out, alu.io.alu_out)
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

  id2ex_data := Mux(valid_current, io.id2ex.bits, id2ex_rst_val)

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

  io.out.hazard.jump_sig           := jump_ctl
  io.out.hazard.rd                 := id2ex_data.data.rd
  io.out.hazard.rd_tag             := id2ex_data.control.reg_w_en
  io.out.csr_hazard.csr_w_addr     := id2ex_data.data.csr_w_addr
  io.out.csr_hazard.csr_w_addr_tag := id2ex_data.control.csr_w_en
}

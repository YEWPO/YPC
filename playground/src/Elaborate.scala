import circt.stage._

object Elaborate extends App {
  def top = new Top
  val options = Seq(
    FirtoolOption(
      "--lowering-options=disallowLocalVariables,disallowPackedArrays,locationInfoStyle=wrapInAtSquareBracket"
    ),
    FirtoolOption("--split-verilog"),
    FirtoolOption("-o=build/verilog_dir/gen"),
    FirtoolOption("--disable-all-randomization")
  )
  val generator = Seq(
    chisel3.stage.ChiselGeneratorAnnotation(() => top),
    CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
  ) ++ options
  (new ChiselStage).execute(args, generator)
}

import chisel3._
import chisel3.util.HasBlackBoxInline

class Ebreak extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val enable = Input(Bool())
  })

  setInline("Ebreak.v",
    """module Ebreak(
      |  input enable
      |);
      |
      |  import "DPI-C" function void ebreak();
      |
      |  always @(*) begin
      |    if (enable) begin
      |      ebreak();
      |    end
      |  end
      |
      |endmodule
    """.stripMargin)
}

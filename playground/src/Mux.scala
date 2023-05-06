import chisel3._
import chisel3.util._

class Mux extends Module {
  val io = IO(new Bundle {
    val regInput = Input(Vec(32, UInt(64.W)))
    val idx = Input(UInt(5.W))
    val out = Output(UInt(64.W))
  })

  io.out := MuxCase(0.U(64.W), Array (
    (io.idx === 0.U) -> io.regInput(0),
    (io.idx === 1.U) -> io.regInput(1),
    (io.idx === 2.U) -> io.regInput(2),
    (io.idx === 3.U) -> io.regInput(3),
    (io.idx === 4.U) -> io.regInput(4),
    (io.idx === 5.U) -> io.regInput(5),
    (io.idx === 6.U) -> io.regInput(6),
    (io.idx === 7.U) -> io.regInput(7),
    (io.idx === 8.U) -> io.regInput(8),
    (io.idx === 9.U) -> io.regInput(9),
    (io.idx === 10.U) -> io.regInput(10),
    (io.idx === 11.U) -> io.regInput(11),
    (io.idx === 12.U) -> io.regInput(12),
    (io.idx === 13.U) -> io.regInput(13),
    (io.idx === 14.U) -> io.regInput(14),
    (io.idx === 15.U) -> io.regInput(15),
    (io.idx === 16.U) -> io.regInput(16),
    (io.idx === 17.U) -> io.regInput(17),
    (io.idx === 18.U) -> io.regInput(18),
    (io.idx === 19.U) -> io.regInput(19),
    (io.idx === 20.U) -> io.regInput(20),
    (io.idx === 21.U) -> io.regInput(21),
    (io.idx === 22.U) -> io.regInput(22),
    (io.idx === 23.U) -> io.regInput(23),
    (io.idx === 24.U) -> io.regInput(24),
    (io.idx === 25.U) -> io.regInput(25),
    (io.idx === 26.U) -> io.regInput(26),
    (io.idx === 27.U) -> io.regInput(27),
    (io.idx === 28.U) -> io.regInput(28),
    (io.idx === 29.U) -> io.regInput(29),
    (io.idx === 30.U) -> io.regInput(30),
    (io.idx === 31.U) -> io.regInput(31)
    ))
}

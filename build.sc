import mill._
import mill.scalalib._
import mill.scalalib.scalafmt.ScalafmtModule
import mill.scalalib.TestModule.Utest

object playground extends ScalaModule with ScalafmtModule { m =>
  def scalaVersion = "2.13.12"
  def scalacOptions = Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit",
    "-unchecked",
    "-Ywarn-dead-code",
    "-Ywarn-unused",
    "-Ymacro-annotations"
  )

  def ivyDeps = Agg(
    ivy"org.chipsalliance::chisel:6.0.0" 
  )
  def scalacPluginIvyDeps = Agg(
    ivy"org.chipsalliance:::chisel-plugin:6.0.0"
  )

  object test extends ScalaTests with Utest {
    def ivyDeps = m.ivyDeps() ++ Agg(
      ivy"com.lihaoyi::utest:0.8.2",
      ivy"edu.berkeley.cs::chiseltest:5.0.2"
    )
  }
}

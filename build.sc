import mill._
import mill.scalalib._
import mill.scalalib.scalafmt.ScalafmtModule

import mill.bsp._

object playground extends ScalaModule with ScalafmtModule { m =>
  override def scalaVersion = "2.13.10"
  override def scalacOptions = Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit",
    "-P:chiselplugin:genBundleElements"
  )
  override def ivyDeps = Agg(
    ivy"edu.berkeley.cs::chisel3:3.6.0",
    ivy"com.sifive::chisel-circt:0.8.0",
  )
  override def scalacPluginIvyDeps = Agg(
    ivy"edu.berkeley.cs:::chisel3-plugin:3.6.0",
  )
  object test extends ScalaTests with TestModule.Utest {
    override def ivyDeps = m.ivyDeps() ++ Agg(
      ivy"com.lihaoyi::utest:0.8.1",
      ivy"edu.berkeley.cs::chiseltest:0.6.0",
    )
  }
}

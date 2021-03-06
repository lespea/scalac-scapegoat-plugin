package com.sksamuel.scapegoat.inspections.string

import com.sksamuel.scapegoat.PluginRunner
import org.scalatest.{FreeSpec, Matchers, OneInstancePerTest}

/** @author Stephen Samuel */
class LooksLikeInterpolatedStringTest extends FreeSpec with Matchers with PluginRunner with OneInstancePerTest {

  override val inspections = Seq(new LooksLikeInterpolatedString)

  "LooksLikeInterpolatedString" - {
    "should report warning" - {
      "for string containing $var" in {
        val code = """object Test {
                      val str = "this is my $interpolated string lookalike"
                   } """.stripMargin
        compileCodeSnippet(code)
        compiler.scapegoat.feedback.warnings.size shouldBe 1
      }
    }
    "for string containing ${var.method}" in {
      val code = """object Test {
                      val str = "this is my ${interpolated.string} lookalike"
                   } """.stripMargin
      compileCodeSnippet(code)
      compiler.scapegoat.feedback.warnings.size shouldBe 1
    }
  }
  "should not report warning" - {
    "for normal string" in {
      val code = """object Test {
                      val str = "this is my not interpolated string lookalike"
                   } """.stripMargin
      compileCodeSnippet(code)
      compiler.scapegoat.feedback.warnings.size shouldBe 0
    }
  }
}

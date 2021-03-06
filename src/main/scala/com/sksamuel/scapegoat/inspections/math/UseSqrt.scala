package com.sksamuel.scapegoat.inspections.math

import com.sksamuel.scapegoat._

/** @author Stephen Samuel */
class UseSqrt extends Inspection {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def postTyperTraverser = Some apply  new context.Traverser {

      import context.global._

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(pack, TermName("pow")), List(_, Literal(Constant(0.5d))))
            if pack.toString() == "scala.math.`package`"
            || pack.toString() == "java.this.lang.Math"
            || pack.toString() == "java.this.lang.StrictMath" =>
            val math = pack.toString().stripPrefix("java.this.lang.").stripPrefix("scala.").stripSuffix(".`package`")
            context.warn(s"Use ${math}.sqrt", tree.pos, Levels.Info,
              s"${math}.sqrt is clearer and more performant than ${math}.pow(x, 0.5)",
              UseSqrt.this)
          case _ => continue(tree)
        }
      }
    }
  }
}

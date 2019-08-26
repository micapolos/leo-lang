package leo13

import leo13.script.lineTo
import leo13.script.script

object Append {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "append" lineTo script()
}

object Resolve {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "resolve" lineTo script()
}


object Lhs {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "lhs" lineTo script()
}

object Rhs {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "rhs" lineTo script()
}

object RhsLine {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "rhsline" lineTo script()
}

object Meta {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "meta" lineTo script()
}

object Quote {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "quote" lineTo script()
}

object Evaluator {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "evaluator" lineTo script()
}

val append = Append
val lhs = Lhs
val rhs = Rhs
val rhsLine = RhsLine
val meta = Meta
val resolve = Resolve
val quote = Quote
val evaluator = Evaluator
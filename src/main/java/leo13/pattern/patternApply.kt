package leo13.pattern

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script

data class PatternApply(val lhsPattern: Pattern, val rhsPattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() = "apply" lineTo script(
			"lhs" lineTo script(lhsPattern.scriptingLine),
			"rhs" lineTo script(rhsPattern.scriptingLine))
}

fun Pattern.apply(rhs: Pattern) = PatternApply(this, rhs)
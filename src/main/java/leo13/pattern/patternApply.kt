package leo13.pattern

import leo13.ObjectScripting
import leo13.applyName
import leo13.leftName
import leo13.rightName
import leo13.script.lineTo
import leo13.script.script

data class PatternApply(val lhsPattern: Pattern, val rhsPattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() = applyName lineTo script(
			leftName lineTo script(lhsPattern.scriptingLine),
			rightName lineTo script(rhsPattern.scriptingLine))
}

fun Pattern.apply(rhs: Pattern) = PatternApply(this, rhs)
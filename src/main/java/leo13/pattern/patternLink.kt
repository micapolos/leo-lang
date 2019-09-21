package leo13.pattern

import leo.base.ifOrNull
import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script

data class PatternLink(val lhs: Pattern, val item: PatternItem) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "link" lineTo script(lhs.scriptingLine, item.scriptLine)
}

infix fun Pattern.linkTo(item: PatternItem) = PatternLink(this, item)

val PatternLink.pattern get() =
	lhs.plus(item)

fun PatternLink.leafPlusOrNull(pattern: Pattern): PatternLink? =
	ifOrNull(lhs.isEmpty) {
		item.leafPlusOrNull(pattern)?.let { lhs linkTo it }
	}

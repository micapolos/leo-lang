package leo13.type

import leo13.LeoObject
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script

sealed class PatternRhs : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "rhs"
	override val scriptableBody get() = script(rhsScriptableLine)
	abstract val rhsScriptableName: String
	abstract val rhsScriptableBody: Script
	val rhsScriptableLine get() = rhsScriptableName lineTo rhsScriptableBody
}

data class PatternPatternRhs(val pattern: Pattern) : PatternRhs() {
	override fun toString() = super.toString()
	override val rhsScriptableName get() = pattern.scriptableName
	override val rhsScriptableBody get() = pattern.scriptableBody
}

data class RecursionPatternRhs(val recursion: Recursion) : PatternRhs() {
	override fun toString() = super.toString()
	override val rhsScriptableName get() = recursion.scriptableName
	override val rhsScriptableBody get() = recursion.scriptableBody
}

fun rhs(pattern: Pattern): PatternRhs = PatternPatternRhs(pattern)
fun rhs(recursion: Recursion): PatternRhs = RecursionPatternRhs(recursion)

fun PatternRhs.contains(thunk: PatternRhs): Boolean =
	when (this) {
		is PatternPatternRhs -> thunk is PatternPatternRhs && pattern.contains(thunk.pattern)
		is RecursionPatternRhs -> thunk is RecursionPatternRhs && recursion == thunk.recursion
	}

val PatternRhs.unsafeStaticScript: Script
	get() =
		when (this) {
			is PatternPatternRhs -> pattern.unsafeStaticScript
			is RecursionPatternRhs -> TODO()
		}

package leo13.type

import leo13.LeoObject
import leo13.script.Script

sealed class PatternRhs : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "thunk"
	override val scriptableBody get() = thunkScriptableBody
	abstract val thunkScriptableBody: Script
}

data class TypePatternRhs(val pattern: Pattern) : PatternRhs() {
	override fun toString() = super.toString()
	override val thunkScriptableBody get() = pattern.scriptableBody
}

data class RecursionPatternRhs(val recursion: Recursion) : PatternRhs() {
	override fun toString() = super.toString()
	override val thunkScriptableBody get() = recursion.scriptableBody
}

fun thunk(pattern: Pattern): PatternRhs = TypePatternRhs(pattern)
fun thunk(recursion: Recursion): PatternRhs = RecursionPatternRhs(recursion)

fun PatternRhs.contains(thunk: PatternRhs): Boolean =
	when (this) {
		is TypePatternRhs -> thunk is TypePatternRhs && pattern.contains(thunk.pattern)
		is RecursionPatternRhs -> thunk is RecursionPatternRhs && recursion == thunk.recursion
	}

val PatternRhs.unsafeStaticScript: Script
	get() =
		when (this) {
			is TypePatternRhs -> pattern.unsafeStaticScript
			is RecursionPatternRhs -> TODO()
		}

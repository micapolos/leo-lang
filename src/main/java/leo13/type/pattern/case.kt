package leo13.type.pattern

import leo13.script.Scriptable

data class Case(val name: String, val rhs: Pattern) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = name
	override val scriptableBody get() = rhs.scriptableBody
}

infix fun String.caseTo(rhs: Pattern) = Case(this, rhs)

fun Case.contains(patternLine: PatternLine): Boolean =
	name == patternLine.name && rhs.contains(patternLine.rhs)

fun Case.contains(case: Case): Boolean =
	name == case.name && rhs.contains(case.rhs)
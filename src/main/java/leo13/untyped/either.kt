package leo13.untyped

import leo.base.notNullIf
import leo.base.notNullOrError
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.onlyLineOrNull
import leo13.script.script
import leo13.untyped.value.ValueLine

data class Either(val name: String, val rhs: Pattern) {
	override fun toString() = scriptLine.toString()
}

infix fun String.eitherTo(rhs: Pattern) = Either(this, rhs)
fun either(name: String) = name eitherTo pattern()

fun Either.matches(scriptLine: ScriptLine): Boolean =
	name == scriptLine.name && rhs.matches(scriptLine.rhs)

fun Either.matches(line: ValueLine): Boolean =
	name == scriptLine.name && rhs.matches(line.rhs)

val ScriptLine.unsafeBodyEither: Either
	get() =
		name eitherTo rhs.unsafeBodyPattern

val ScriptLine.unsafeEither: Either
	get() =
		rhsOrNull(eitherName)
			.notNullOrError("either expected")
			.onlyLineOrNull
			.notNullOrError("either line expected")
			.run { name eitherTo rhs.unsafeBodyPattern }

val Either.bodyScriptLine: ScriptLine
	get() =
		name lineTo rhs.bodyScript

val Either.scriptLine: ScriptLine
	get() =
		eitherName lineTo script(bodyScriptLine)

fun Either.linePatternOrNull(name: String): Pattern? =
	notNullIf(this.name == name) {
		pattern(name lineTo rhs)
	}

fun Either.replaceLineOrNull(line: PatternLine): Either? =
	notNullIf(name == line.name) {
		name eitherTo line.rhs
	}

val Either.patternLine
	get() =
		name lineTo rhs
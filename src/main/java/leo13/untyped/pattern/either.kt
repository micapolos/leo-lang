package leo13.untyped.pattern

import leo.base.notNullIf
import leo.base.notNullOrError
import leo13.ObjectScripting
import leo13.eitherName
import leo13.script.*
import leo13.untyped.value.ValueLine

data class Either(val name: String, val rhs: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = scriptLine
}

infix fun String.eitherTo(rhs: Pattern) = Either(this, rhs)
fun either(name: String) = name eitherTo pattern()

fun Either.matches(scriptLine: ScriptLine): Boolean =
	name == scriptLine.name && rhs.matches(scriptLine.rhs)

fun Either.matches(line: ValueLine): Boolean =
	name == scriptLine.name && rhs.matches(line.rhs)

fun Either.contains(line: PatternLine): Boolean =
	name == line.name && rhs.contains(line.rhs)

fun Either.contains(either: Either): Boolean =
	name == either.name && rhs.contains(either.rhs)

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

fun Either.patternLineOrNull(name: String): PatternLine? =
	notNullIf(this.name == name) {
		name lineTo rhs
	}

fun Either.replaceLineOrNull(line: PatternLine): Either? =
	notNullIf(name == line.name) {
		name eitherTo line.rhs
	}

val Either.patternLine
	get() =
		name lineTo rhs

val PatternLine.either
	get() =
		name eitherTo rhs

fun Either.leafPlusOrNull(pattern: Pattern): Either? =
	rhs.leafPlusOrNull(pattern)?.let { name eitherTo it }
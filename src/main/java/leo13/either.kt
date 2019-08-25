package leo13

import leo.base.ifOrNull

data class Either(val name: String, val type: Type) {
	override fun toString() = asFirstScriptLine.toString()
}

infix fun String.eitherTo(type: Type) = Either(this, type)

val Either.asFirstScriptLine get() = name lineTo type.asScript
val Either.asNextScriptLine get() = "or" lineTo script(asFirstScriptLine)

val TypeLine.either
	get() =
		name eitherTo rhs

val ScriptLine.firstEither
	get() =
		name eitherTo rhs.type

val ScriptLine.nextEitherOrNull: Either?
	get() =
		ifOrNull(name == "or") {
			rhs.onlyLineOrNull?.firstEither
		}

fun Either.matches(scriptLine: ScriptLine): Boolean =
	name == scriptLine.name && type.matches(scriptLine.rhs)

fun Either.contains(either: Either) =
	name == either.name && type.contains(either.type)

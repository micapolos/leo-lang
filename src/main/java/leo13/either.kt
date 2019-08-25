package leo13

import leo.base.ifOrNull

data class Either(val name: String, val type: Type) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine get() = "either" lineTo script(name lineTo type.asScript)
}

infix fun String.eitherTo(type: Type) = Either(this, type)
fun either(name: String, type: Type = type()) = Either(name, type)

val Either.asFirstScriptLine get() = name lineTo type.asScript
val Either.asNextScriptLine get() = "or" lineTo script(asFirstScriptLine)

val TypeLine.either
	get() =
		name eitherTo rhs

val ScriptLine.eitherOrNull: Either?
	get() =
		ifOrNull(name == "either") {
			rhs.onlyLineOrNull?.let { onlyLine ->
				onlyLine.rhs.typeOrNull?.let { type ->
					either(onlyLine.name, type)
				}
			}
		}

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

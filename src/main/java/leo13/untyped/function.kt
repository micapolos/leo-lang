package leo13.untyped

import leo.base.notNullIf
import leo13.script.*

data class Function(val pattern: Pattern, val body: Body) {
	override fun toString() = super.toString()
}

fun function(pattern: Pattern, body: Body) =
	Function(pattern, body)

fun Function.bodyOrNull(script: Script): Body? =
	notNullIf(pattern.matches(script)) { body }

val functionName = "function"

val functionReader: Reader<Function>
	get() =
	reader(
		functionName,
		patternReader,
		bodyReader,
		::function)

val functionWriter: Writer<Function>
	get() =
	writer(
		functionName,
		field(patternWriter) { pattern },
		field(bodyWriter) { body })

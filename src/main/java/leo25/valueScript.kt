package leo25

import leo14.*
import leo14.untyped.scriptLine

val Value?.orNullScript: Script
	get() =
		this?.script ?: script()

val Value.script: Script
	get() =
		when (this) {
			is FunctionValue -> script("native")
			is StringValue -> script("native")
			is StructValue -> struct.script
			is WordValue -> script(word.string)
		}

val Struct.script: Script
	get() =
		tail.orNullScript.plus(head.scriptLine)

val Field.scriptLine: ScriptLine
	get() = null
		?: literalOrNull?.scriptLine
		?: word.string lineTo value.script

val Field.literalOrNull: Literal?
	get() =
		when (word.string) {
			"text" -> (value as? StringValue)?.string?.literal
			else -> null
		}
package leo23.type

import leo13.Stack
import leo13.map
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.plus
import leo14.script
import leo14.scriptLine

val Type.scriptLine: ScriptLine
	get() =
		when (this) {
			BooleanType -> "boolean".scriptLine
			TextType -> "text".scriptLine
			NumberType -> "number".scriptLine
			is ArrowType -> "arrow" lineTo paramTypes.map { it.scriptLine }.script.plus("to" lineTo script(returnType.scriptLine))
			is StructType -> name lineTo fields.map { it.scriptLine }.script
			is ChoiceType -> name lineTo script("choice" lineTo cases.map { it.scriptLine }.script)
		}

val Stack<Type>.script: Script
	get() =
		map { scriptLine }.script

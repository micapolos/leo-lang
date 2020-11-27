package leo23.term.type

import leo14.ScriptLine
import leo14.lineTo
import leo14.plus
import leo14.script
import leo14.scriptLine

val Type.scriptLine: ScriptLine
	get() =
		when (this) {
			NilType -> "nil".scriptLine
			BooleanType -> "boolean".scriptLine
			TextType -> "text".scriptLine
			NumberType -> "number".scriptLine
			is TupleType -> "tuple" lineTo itemTypes.map { it.scriptLine }.script
			is ChoiceType -> "choice" lineTo itemTypes.map { it.scriptLine }.script
			is ArrowType -> "arrow" lineTo paramTypes.map { it.scriptLine }.script.plus("to" lineTo script(returnType.scriptLine))
		}
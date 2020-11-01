package leo21.typed

import leo13.linkOrNull
import leo14.Script
import leo14.ScriptLine
import leo14.lambda.native
import leo14.lambda.pair
import leo14.lambda.value.double
import leo14.lambda.value.string
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script
import leo21.type.ArrowLine
import leo21.type.ChoiceType
import leo21.type.DoubleLine
import leo21.type.FieldLine
import leo21.type.StringLine
import leo21.type.Struct
import leo21.type.StructType

val Typed.script: Script
	get() =
		when (type) {
			is StructType -> struct.script
			is ChoiceType -> script(choice.scriptLine)
		}

val StructTyped.script: Script
	get() =
		struct.lineStack.linkOrNull
			?.let { link ->
				valueTerm.pair().let { (lhs, rhs) ->
					StructTyped(lhs, Struct(link.stack)).script
						.plus(LineTyped(rhs, link.value).scriptLine)
				}
			} ?: script()

val LineTyped.scriptLine: ScriptLine
	get() =
		when (line) {
			StringLine -> leo14.line(literal(valueTerm.native.string))
			DoubleLine -> leo14.line(literal(valueTerm.native.double))
			is ArrowLine -> ArrowTyped(valueTerm, line.arrow).scriptLine
			is FieldLine -> FieldTyped(valueTerm, line.field).scriptLine
		}

val ChoiceTyped.scriptLine: ScriptLine
	get() =
		TODO()

val ArrowTyped.scriptLine: ScriptLine
	get() =
		TODO()

val FieldTyped.scriptLine: ScriptLine
	get() =
		field.name lineTo rhs.script
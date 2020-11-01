package leo21.typed

import leo14.Script
import leo14.ScriptLine
import leo14.lambda.native
import leo14.lambda.value.double
import leo14.lambda.value.string
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script

val Typed.script: Script
	get() =
		switch(StructTyped::script, ChoiceTyped::script)

val StructTyped.script: Script
	get() =
		decompileLinkOrNull
			?.let { link -> link.tail.script.plus(link.head.scriptLine) }
			?: script()

val LineTyped.scriptLine: ScriptLine
	get() =
		switch(
			StringTyped::scriptLine,
			DoubleTyped::scriptLine,
			FieldTyped::scriptLine,
			ArrowTyped::scriptLine)

val ChoiceTyped.script: Script
	get() =
		TODO()

val StringTyped.scriptLine: ScriptLine
	get() =
		leo14.line(literal(valueTerm.native.string))

val DoubleTyped.scriptLine: ScriptLine
	get() =
		leo14.line(literal(valueTerm.native.double))

val FieldTyped.scriptLine: ScriptLine
	get() =
		field.name lineTo rhsTyped.script

val ArrowTyped.scriptLine: ScriptLine
	get() =
		TODO()

package leo21.evaluated

import leo13.Link
import leo14.Script
import leo14.ScriptLine
import leo14.lambda.value.native
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script
import leo21.prim.number
import leo21.prim.string
import leo21.type.scriptLine

val Evaluated.script: Script
	get() =
		linkOrNull
			?.evaluatedScript
			?: script()

val Link<Evaluated, LineEvaluated>.evaluatedScript
	get() =
		evaluated.script.plus(lineEvaluated.scriptLine)

val LineEvaluated.scriptLine: ScriptLine
	get() =
		switch(
			StringEvaluated::scriptLine,
			DoubleEvaluated::scriptLine,
			FieldEvaluated::scriptLine,
			ChoiceEvaluated::scriptLine,
			ArrowEvaluated::scriptLine)

val StringEvaluated.scriptLine: ScriptLine
	get() =
		line(literal(value.native.string))

val DoubleEvaluated.scriptLine: ScriptLine
	get() =
		line(literal(value.native.number))

val FieldEvaluated.scriptLine: ScriptLine
	get() =
		name lineTo rhs.script

val ChoiceEvaluated.scriptLine: ScriptLine
	get() =
		switch(
			ChoiceEvaluated::scriptLine,
			LineEvaluated::scriptLine)

val ArrowEvaluated.scriptLine: ScriptLine
	get() =
		arrow.scriptLine
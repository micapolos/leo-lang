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
import leo21.type.printScriptLine

val Evaluated.printScript: Script
	get() =
		linkOrNull
			?.evaluatedPrintScript
			?: script()

val Link<Evaluated, LineEvaluated>.evaluatedPrintScript
	get() =
		evaluated.printScript.plus(lineEvaluated.printScriptLine)

val LineEvaluated.printScriptLine: ScriptLine
	get() =
		switch(
			StringEvaluated::printScriptLine,
			DoubleEvaluated::printScriptLine,
			FieldEvaluated::printScriptLine,
			ChoiceEvaluated::printScriptLine,
			ArrowEvaluated::printScriptLine)

val StringEvaluated.printScriptLine: ScriptLine
	get() =
		line(literal(value.native.string))

val DoubleEvaluated.printScriptLine: ScriptLine
	get() =
		line(literal(value.native.number))

val FieldEvaluated.printScriptLine: ScriptLine
	get() =
		name lineTo rhs.printScript

val ChoiceEvaluated.printScriptLine: ScriptLine
	get() =
		switch(
			ChoiceEvaluated::printScriptLine,
			LineEvaluated::printScriptLine)

val ArrowEvaluated.printScriptLine: ScriptLine
	get() =
		arrow.printScriptLine
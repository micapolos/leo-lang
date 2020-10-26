package leo20

import leo.base.fold
import leo.base.reverse
import leo14.FieldScriptLine
import leo14.Literal
import leo14.LiteralScriptLine
import leo14.NumberLiteral
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.StringLiteral
import leo14.lineSeq

val Script.value: Value
	get() =
		value().fold(lineSeq.reverse) { plus(it) }

fun Value.plus(scriptLine: ScriptLine) =
	plus(scriptLine.valueLine)

val ScriptLine.valueLine
	get() =
		when (this) {
			is LiteralScriptLine -> literal.valueLine
			is FieldScriptLine -> field.valueLine
		}

val ScriptField.valueLine
	get() =
		string lineTo rhs.value

val Literal.valueLine
	get() =
		when (this) {
			is StringLiteral -> line(string)
			is NumberLiteral -> line(number.bigDecimal)
		}

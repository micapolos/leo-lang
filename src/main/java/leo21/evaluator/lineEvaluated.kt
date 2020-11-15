package leo21.evaluator

import leo14.Literal
import leo14.Number
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.lambda.value.Value
import leo14.lambda.value.term
import leo14.lambda.value.value
import leo21.compiled.LineCompiled
import leo21.compiled.of
import leo21.prim.Prim
import leo21.prim.prim
import leo21.type.Line
import leo21.type.doubleLine
import leo21.type.lineTo
import leo21.type.stringLine

data class LineEvaluated(val value: Value<Prim>, val line: Line)

infix fun Value<Prim>.of(line: Line) = LineEvaluated(this, line)

infix fun String.lineTo(evaluated: Evaluated): LineEvaluated =
	evaluated.value of (this lineTo evaluated.type)

val LineEvaluated.compiled: LineCompiled
	get() =
		value.term of line

val Literal.lineEvaluated: LineEvaluated
	get() =
		when (this) {
			is StringLiteral -> string.lineEvaluated
			is NumberLiteral -> number.lineEvaluated
		}

val String.lineEvaluated: LineEvaluated get() = value(prim) of stringLine
val Double.lineEvaluated: LineEvaluated get() = value(prim) of stringLine
val Number.lineEvaluated: LineEvaluated get() = value(prim) of doubleLine

package leo21.evaluator

import leo14.lambda.value.Value
import leo14.lambda.value.term
import leo21.compiled.LineCompiled
import leo21.compiled.of
import leo21.prim.Prim
import leo21.type.Line

data class LineEvaluated(val value: Value<Prim>, val line: Line)

infix fun Value<Prim>.of(line: Line) = LineEvaluated(this, line)

val LineEvaluated.compiled: LineCompiled
	get() =
		value.term of line
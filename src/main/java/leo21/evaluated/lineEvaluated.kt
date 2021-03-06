package leo21.evaluated

import leo.base.notNullIf
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
import leo21.type.ArrowLine
import leo21.type.ChoiceLine
import leo21.type.FieldLine
import leo21.type.Line
import leo21.type.NumberLine
import leo21.type.RecurseLine
import leo21.type.RecursiveLine
import leo21.type.StringLine
import leo21.type.fieldOrNull
import leo21.type.isEmpty
import leo21.type.line
import leo21.type.lineTo
import leo21.type.numberLine
import leo21.type.resolve
import leo21.type.stringLine

data class LineEvaluated(val value: Value<Prim>, val line: Line)

infix fun Value<Prim>.of(line: Line) = LineEvaluated(this, line)

infix fun String.lineTo(evaluated: Evaluated): LineEvaluated =
	evaluated.value of (this lineTo evaluated.type)

val LineEvaluated.compiled: LineCompiled
	get() =
		value.term of line

fun <R> LineEvaluated.switch(
	stringFn: (StringEvaluated) -> R,
	doubleFn: (DoubleEvaluated) -> R,
	fieldFn: (FieldEvaluated) -> R,
	choiceFn: (ChoiceEvaluated) -> R,
	arrowFn: (ArrowEvaluated) -> R
): R =
	when (line) {
		StringLine -> stringFn(StringEvaluated(value))
		NumberLine -> doubleFn(DoubleEvaluated(value))
		is FieldLine -> fieldFn(FieldEvaluated(value, line.field))
		is ChoiceLine -> choiceFn(ChoiceEvaluated(value, line.choice))
		is ArrowLine -> arrowFn(ArrowEvaluated(value, line.arrow))
		is RecursiveLine -> value.of(line.recursive.resolve).switch(stringFn, doubleFn, fieldFn, choiceFn, arrowFn)
		is RecurseLine -> null!!
	}

val Literal.lineEvaluated: LineEvaluated
	get() =
		when (this) {
			is StringLiteral -> string.lineEvaluated
			is NumberLiteral -> number.lineEvaluated
		}

val ChoiceEvaluated.lineEvaluated: LineEvaluated
	get() =
		valueOrNull!! of line(choice)

val LineEvaluated.fieldOrNull: FieldEvaluated?
	get() =
		switch({ null }, { null }, { it }, { null }, { null })

val LineEvaluated.choiceOrNull: ChoiceEvaluated?
	get() =
		switch({ null }, { null }, { null }, { it }, { null })

val LineEvaluated.arrowOrNull: ArrowEvaluated?
	get() =
		switch({ null }, { null }, { null }, { null }, { it })

val LineEvaluated.onlyNameOrNull: String?
	get() =
		line.fieldOrNull?.let { field ->
			notNullIf(field.rhs.isEmpty) {
				field.name
			}
		}

val String.lineEvaluated: LineEvaluated get() = value(prim) of stringLine
val Double.lineEvaluated: LineEvaluated get() = value(prim) of numberLine
val Number.lineEvaluated: LineEvaluated get() = value(prim) of numberLine

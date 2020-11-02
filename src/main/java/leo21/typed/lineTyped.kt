package leo21.typed

import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.lambda.Term
import leo14.lambda.term
import leo14.lambda.value.Value
import leo14.lambda.value.value
import leo21.type.ArrowLine
import leo21.type.DoubleLine
import leo21.type.FieldLine
import leo21.type.Line
import leo21.type.StringLine
import leo21.type.doubleLine
import leo21.type.lineTo
import leo21.type.stringLine

data class LineTyped(val valueTerm: Term<Value>, val line: Line)

fun <R> LineTyped.switch(
	stringFn: (StringTyped) -> R,
	doubleFn: (DoubleTyped) -> R,
	fieldFn: (FieldTyped) -> R,
	arrowFn: (ArrowTyped) -> R
): R =
	when (line) {
		StringLine -> stringFn(StringTyped(valueTerm))
		DoubleLine -> doubleFn(DoubleTyped(valueTerm))
		is FieldLine -> fieldFn(FieldTyped(valueTerm, line.field))
		is ArrowLine -> arrowFn(ArrowTyped(valueTerm, line.arrow))
	}

fun line(string: String) = LineTyped(term(value(string)), stringLine)
fun line(double: Double) = LineTyped(term(value(double)), doubleLine)
infix fun String.lineTo(rhs: Typed) = LineTyped(rhs.valueTerm, this lineTo rhs.type)

fun lineTyped(literal: Literal): LineTyped =
	when (literal) {
		is StringLiteral -> line(literal.string)
		is NumberLiteral -> line(literal.number.bigDecimal.toDouble())
	}

val LineTyped.stringTypedOrNull: StringTyped?
	get() =
		switch({ it }, { null }, { null }, { null })
val LineTyped.doubleTypedOrNull: DoubleTyped?
	get() =
		switch({ null }, { it }, { null }, { null })
val LineTyped.arrowTypedOrNull: ArrowTyped?
	get() =
		switch({ null }, { null }, { null }, { it })
val LineTyped.fieldTypedOrNull: FieldTyped?
	get() =
		switch({ null }, { null }, { it }, { null })

val LineTyped.rhsOrNull: Typed? get() = fieldTypedOrNull?.rhsTyped

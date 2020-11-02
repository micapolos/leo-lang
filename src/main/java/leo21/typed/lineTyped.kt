package leo21.typed

import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.lambda.Term
import leo14.lambda.term
import leo21.prim.Prim
import leo21.prim.prim
import leo21.type.ArrowLine
import leo21.type.DoubleLine
import leo21.type.FieldLine
import leo21.type.Line
import leo21.type.StringLine
import leo21.type.doubleLine
import leo21.type.line
import leo21.type.lineTo
import leo21.type.stringLine

data class LineTyped(val term: Term<Prim>, val line: Line)

fun <R> LineTyped.switch(
	stringFn: (StringTyped) -> R,
	doubleFn: (DoubleTyped) -> R,
	fieldFn: (FieldTyped) -> R,
	arrowFn: (ArrowTyped) -> R
): R =
	when (line) {
		StringLine -> stringFn(StringTyped(term))
		DoubleLine -> doubleFn(DoubleTyped(term))
		is FieldLine -> fieldFn(FieldTyped(term, line.field))
		is ArrowLine -> arrowFn(ArrowTyped(term, line.arrow))
	}

fun line(string: String) = LineTyped(term(prim(string)), stringLine)
fun line(double: Double) = LineTyped(term(prim(double)), doubleLine)
infix fun String.lineTo(rhs: Typed) = LineTyped(rhs.term, this lineTo rhs.type)
fun lineTyped(arrowTyped: ArrowTyped) = LineTyped(arrowTyped.term, line(arrowTyped.arrow))

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

package leo21.typed

import leo14.lambda.Term
import leo14.lambda.term
import leo14.lambda.value.Value
import leo14.lambda.value.value
import leo21.type.ArrowLine
import leo21.type.DoubleLine
import leo21.type.FieldLine
import leo21.type.Line
import leo21.type.StringLine
import leo21.type.arrowOrNull
import leo21.type.doubleLine
import leo21.type.fieldOrNull
import leo21.type.lineTo
import leo21.type.stringLine

data class LineTyped(val valueTerm: Term<Value>, val line: Line)

fun line(string: String) = LineTyped(term(value(string)), stringLine)
fun line(double: Double) = LineTyped(term(value(double)), doubleLine)
infix fun String.lineTo(rhs: Typed) = LineTyped(rhs.valueTerm, this lineTo rhs.type)

val LineTyped.arrowTypedOrNull: ArrowTyped? get() = line.arrowOrNull?.let { ArrowTyped(valueTerm, it) }
val LineTyped.fieldTypedOrNull: FieldTyped? get() = line.fieldOrNull?.let { FieldTyped(valueTerm, it) }

val LineTyped.rhsOrNull: Typed?
	get() =
		when (line) {
			StringLine -> null
			DoubleLine -> null
			is ArrowLine -> null
			is FieldLine -> Typed(valueTerm, line.field.rhs)
		}
package leo21.typed

import leo.base.notNullIf
import leo14.lambda.invoke
import leo21.value.DoubleMinusDoubleValue
import leo21.value.DoublePlusDoubleValue
import leo21.value.DoubleTimesDoubleValue
import leo21.value.StringPlusStringValue
import leo21.value.Value
import leo21.type.Line
import leo21.type.Type
import leo21.type.doubleLine
import leo21.type.doubleType
import leo21.type.lineTo
import leo21.type.name
import leo21.type.stringLine
import leo21.type.stringType
import leo21.type.type

fun Typed.resolveBinaryOp(inputLine: Line, opValue: Value, opName: String, outputType: Type): Typed? =
	notNullIf(type == type(inputLine, opName lineTo type(inputLine))) {
		Typed(
			leo14.lambda.term(opValue)
				.invoke(make("given").get(inputLine.name).valueTerm)
				.invoke(make("given").get(opName).get(inputLine.name).valueTerm),
			outputType)
	}

val Typed.resolveOrNull: Typed?
	get() =
		null
			?: resolveBinaryOp(doubleLine, DoublePlusDoubleValue, "plus", doubleType)
			?: resolveBinaryOp(doubleLine, DoubleMinusDoubleValue, "minus", doubleType)
			?: resolveBinaryOp(doubleLine, DoubleTimesDoubleValue, "times", doubleType)
			?: resolveBinaryOp(stringLine, StringPlusStringValue, "plus", stringType)


val Typed.resolve: Typed
	get() =
		resolveOrNull ?: this
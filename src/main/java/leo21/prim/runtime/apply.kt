package leo21.prim.runtime

import leo14.Number
import leo14.anyReflectScriptLine
import leo14.cosinus
import leo14.lambda.value.Value
import leo14.lambda.value.eitherFirst
import leo14.lambda.value.eitherSecond
import leo14.lambda.value.native
import leo14.lambda.value.pair
import leo14.lambda.value.value
import leo14.minus
import leo14.number
import leo14.orError
import leo14.plus
import leo14.sinus
import leo14.string
import leo14.times
import leo21.evaluated.nilValue
import leo21.prim.NilPrim
import leo21.prim.NumberCosinusPrim
import leo21.prim.NumberEqualsNumberPrim
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.NumberPrim
import leo21.prim.NumberSinusPrim
import leo21.prim.NumberStringPrim
import leo21.prim.NumberTimesNumberPrim
import leo21.prim.Prim
import leo21.prim.StringLengthPrim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim
import leo21.prim.StringTryNumberPrim
import leo21.prim.number
import leo21.prim.prim
import leo21.prim.string
import leo22.dsl.*

fun Prim.apply(rhs: Value<Prim>): Value<Prim> =
	when (this) {
		is NilPrim -> null
		is StringPrim -> null
		is NumberPrim -> null
		NumberPlusNumberPrim -> rhs.apply(Prim::number, Number::plus, Prim::number, Number::prim)
		NumberMinusNumberPrim -> rhs.apply(Prim::number, Number::minus, Prim::number, Number::prim)
		NumberTimesNumberPrim -> rhs.apply(Prim::number, Number::times, Prim::number, Number::prim)
		NumberEqualsNumberPrim -> TODO()
		StringPlusStringPrim -> rhs.apply(Prim::string, String::plus, Prim::string, String::prim)
		NumberStringPrim -> rhs.apply(Prim::number, Number::string, String::prim)
		NumberSinusPrim -> rhs.apply(Prim::number, Number::sinus, Number::prim)
		NumberCosinusPrim -> rhs.apply(Prim::number, Number::cosinus, Number::prim)
		StringLengthPrim -> rhs.apply(Prim::string, String::lengthNumber, Number::prim)
		StringTryNumberPrim -> rhs.stringTryNumber
	}.orError(anyReflectScriptLine, apply(rhs.anyReflectScriptLine))

fun <Lhs, Rhs, Out> Value<Prim>.apply(
	lhs: Prim.() -> Lhs,
	op: Lhs.(Rhs) -> Out,
	rhs: Prim.() -> Rhs,
	out: Out.() -> Prim
): Value<Prim> =
	pair { lhs, rhs ->
		value(lhs.native.lhs().op(rhs.native.rhs()).out())
	}

fun <Lhs, Out> Value<Prim>.apply(
	lhs: Prim.() -> Lhs,
	op: Lhs.() -> Out,
	out: Out.() -> Prim
): Value<Prim> =
	value(native.lhs().op().out())

val String.lengthNumber: Number
	get() =
		length.number

val String.numberOrNull: Number?
	get() =
		try {
			number(toBigDecimal())
		} catch (numberFormatException: NumberFormatException) {
			null
		}

val Value<Prim>.stringTryNumber: Value<Prim>
	get() =
		try_ { value(prim(native.string.numberOrNull!!)) }

fun Value<Prim>.try_(fn: Value<Prim>.() -> Value<Prim>): Value<Prim> =
	try {
		fn().trySuccess
	} catch (throwable: Throwable) {
		tryFailureValue
	}

val Value<Prim>.trySuccess: Value<Prim> get() = eitherSecond.eitherFirst
val tryFailureValue: Value<Prim> get() = nilValue.eitherSecond

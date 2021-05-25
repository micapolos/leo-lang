package leo25

import leo14.*
import leo14.Number

val textAnyLine = textName lineTo script(anyName)
val numberAnyLine = numberName lineTo script(anyName)

fun nativeDefinition(script: Script, fn: Resolver.() -> Value) =
	definition(pattern(script), binding(resolver().function(body(fn))))

fun Resolver.nativeValue(name: String): Value =
	resolutionOrNull(value(name))!!.bindingOrNull!!.valueOrNull!!

fun Value.nativeValue(name: String): Value =
	getOrNull(name)!!

val Value.nativeText: String get() = textOrThrow
val Value.nativeNumber: Number get() = numberOrThrow

val String.nativeValue get() = value(field(literal(this)))
val Number.nativeValue get() = value(field(literal(this)))

val textAppendTextDefinition
	get() =
		nativeDefinition(
			script(textAnyLine, appendName lineTo script(textAnyLine))
		) {
			nativeValue(textName).nativeText
				.plus(nativeValue(appendName).nativeValue(textName).nativeText)
				.nativeValue
		}

val numberPlusNumberDefinition
	get() =
		nativeDefinition(
			script(numberAnyLine, plusName lineTo script(numberAnyLine))
		) {
			nativeValue(numberName).nativeNumber
				.plus(nativeValue(plusName).nativeValue(numberName).nativeNumber)
				.nativeValue
		}

val numberMinusNumberDefinition
	get() =
		nativeDefinition(
			script(numberAnyLine, minusName lineTo script(numberAnyLine))
		) {
			nativeValue(numberName).nativeNumber
				.minus(nativeValue(minusName).nativeValue(numberName).nativeNumber)
				.nativeValue
		}

val numberTimesNumberDefinition
	get() =
		nativeDefinition(
			script(numberAnyLine, timesName lineTo script(numberAnyLine))
		) {
			nativeValue(numberName).nativeNumber
				.times(nativeValue(timesName).nativeValue(numberName).nativeNumber)
				.nativeValue
		}

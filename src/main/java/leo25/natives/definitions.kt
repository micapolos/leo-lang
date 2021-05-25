package leo25.natives

import leo14.*
import leo14.Number
import leo25.*

val textAnyLine = textName lineTo script(anyName)
val numberAnyLine = numberName lineTo script(anyName)

fun nativeDefinition(script: Script, fn: Dictionary.() -> Value) =
	definition(pattern(script), binding(resolver().function(body(fn))))

fun Dictionary.nativeValue(name: String): Value =
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
			textName, { nativeText },
			appendName,
			textName, { nativeText },
			{ nativeValue }, String::plus
		)

val numberPlusNumberDefinition
	get() =
		nativeDefinition(
			numberName, { nativeNumber },
			plusName,
			numberName, { nativeNumber },
			{ nativeValue }, Number::plus
		)

val numberMinusNumberDefinition
	get() =
		nativeDefinition(
			numberName, { nativeNumber },
			minusName,
			numberName, { nativeNumber },
			{ nativeValue }, Number::minus
		)

val numberTimesNumberDefinition
	get() =
		nativeDefinition(
			numberName, { nativeNumber },
			timesName,
			numberName, { nativeNumber },
			{ nativeValue }, Number::times
		)


inline fun <L, R, O> nativeDefinition(
	lhsType: String,
	crossinline lhsFn: Value.() -> L,
	opName: String,
	rhsType: String,
	crossinline rhsFn: Value.() -> R,
	crossinline retFn: O.() -> Value,
	crossinline fn: L.(R) -> O
): Definition =
	nativeDefinition(
		script(
			lhsType lineTo script(anyName),
			opName lineTo script(
				rhsType lineTo script(anyName)
			)
		)
	) {
		nativeValue(lhsType).lhsFn()
			.fn(nativeValue(opName).nativeValue(rhsType).rhsFn())
			.retFn()
	}

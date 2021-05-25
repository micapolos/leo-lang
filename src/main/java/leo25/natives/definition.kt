package leo25.natives

import leo14.Script
import leo14.lineTo
import leo14.script
import leo25.*

fun nativeDefinition(script: Script, fn: Dictionary.() -> Value) =
	definition(pattern(script), binding(dictionary().function(body(fn))))

inline fun <L, O> nativeDefinition(
	lhsType: String,
	crossinline lhsFn: Value.() -> L,
	opName: String,
	crossinline retFn: O.() -> Value,
	crossinline fn: L.() -> O
): Definition =
	nativeDefinition(script(lhsType lineTo script(anyName), opName lineTo script())) {
		nativeValue(lhsType).lhsFn().fn().retFn()
	}

inline fun <L, O> nativeDefinition(
	lhsType: String,
	crossinline lhsFn: Value.() -> L,
	opName: String,
	crossinline retFn: O.() -> Value,
	retType: String,
	crossinline fn: L.() -> O
): Definition =
	nativeDefinition(script(lhsType lineTo script(anyName), opName lineTo script())) {
		value(retType fieldTo nativeValue(lhsType).lhsFn().fn().retFn())
	}

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

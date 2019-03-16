package leo32

import leo.base.Seq
import leo.base.ifNotNull
import leo.base.ifNotNullOr
import leo.base.seqNodeOrNull
import leo.binary.Bit

data class Match<T>(
	val opOrNull: Op<T>?,
	val nextFunctionOrNull: Function<T>?)

fun <T> emptyMatch() =
	Match<T>(null, null)

val <T> Op<T>.match
	get() =
		Match(this, null)

val <T> Function<T>.match
	get() =
		Match(null, this)

fun <T> Match<T>.invoke(scope: Scope<T>) =
	scope
		.ifNotNull(opOrNull) { it.invoke(scope) }
		.goto(nextFunctionOrNull)

val <T> Match<T>.functionForDefine
	get() =
		nextFunctionOrNull ?: emptyFunction()

fun <T> Match<T>.define(bits: Seq<Bit>, match: Match<T>): Match<T> =
	bits
		.seqNodeOrNull
		.ifNotNullOr(
			{ functionForDefine.define(it, match).match },
			{ match })

val <T> Match<T>?.forDefine
	get() =
		this ?: emptyMatch()

fun <T> Match<T>.next(function: Function<T>) =
	copy(nextFunctionOrNull = function)
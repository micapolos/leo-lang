package leo32

import leo.base.Seq
import leo.base.ifNotNull
import leo.base.ifNotNullOr
import leo.base.seqNodeOrNull
import leo.binary.Bit

data class Match(
	val opOrNull: Op?,
	val nextFunctionOrNull: Function?)

val emptyMatch =
	Match(null, null)

val Op.match
	get() =
		Match(this, null)

val Function.match
	get() =
		Match(null, this)

fun Match.invoke(bit: Bit, runtime: Runtime) =
	runtime
		.ifNotNull(opOrNull) { it.invoke(bit, runtime) }
		.goto(nextFunctionOrNull)

val Match.functionForDefine
	get() =
		nextFunctionOrNull ?: emptyFunction

fun Match.define(bits: Seq<Bit>, match: Match): Match =
	bits
		.seqNodeOrNull
		.ifNotNullOr(
			{ functionForDefine.define(it, match).match },
			{ match })

val Match?.forDefine
	get() =
		this ?: emptyMatch

fun Match.next(function: Function) =
	copy(nextFunctionOrNull = function)
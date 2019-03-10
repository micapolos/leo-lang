package leo32

import leo.base.Sequence
import leo.base.ifNotNullOr
import leo.base.nonEmptySequenceOrNull
import leo.binary.Bit

sealed class Match {
	abstract fun invoke(bit: Bit, runtime: Runtime): Runtime?
}

data class PartialMatch(
	val function: Function) : Match() {

	override fun invoke(bit: Bit, runtime: Runtime) =
		runtime.push(function)
}

data class OpMatch(
	val op: Op) : Match() {

	override fun invoke(bit: Bit, runtime: Runtime) =
		op.invoke(bit, runtime)
}

val Function.match get() = PartialMatch(this)
val Op.match get() = OpMatch(this)

val Function.partialMatch
	get() =
		PartialMatch(this)

val Match?.notNull
	get() =
		this ?: emptyFunction.partialMatch

val Match.functionForDefine
	get() =
		when (this) {
			is PartialMatch -> function
			is OpMatch -> emptyFunction
		}

fun Match.define(bits: Sequence<Bit>, match: Match): Match =
	bits
		.nonEmptySequenceOrNull
		.ifNotNullOr(
			{ functionForDefine.define(it, match).partialMatch },
			{ match })

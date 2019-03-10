package leo32

import leo.base.Seq
import leo.base.ifNotNullOr
import leo.base.seqNodeOrNull
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

fun Match.define(bits: Seq<Bit>, match: Match): Match =
	bits
		.seqNodeOrNull
		.ifNotNullOr(
			{ functionForDefine.define(it, match).partialMatch },
			{ match })

package leo32

import leo.base.Seq
import leo.base.SeqNode
import leo.base.ifNotNullOr
import leo.base.seqNodeOrNull
import leo.binary.*

data class Function<T>(
	val matchMap: Arr1<Match<T>?>) {
	override fun toString() = code
}

fun <T> emptyFunction() =
	Function<T>(nullArr1())

fun <T> function(match0: Match<T>, match1: Match<T>) =
	Function(nullArr1<Match<T>>().put(zero.bit, match0).put(one.bit, match1))

fun <T> Function<T>.put(bit: Bit, match: Match<T>) =
	Function(matchMap.put(bit, match))

fun <T> Function<T>.define(bit: Bit, match: Match<T>) =
	copy(matchMap = matchMap.put(bit, match))

fun <T> Function<T>.define(bits: SeqNode<Bit>, match: Match<T>): Function<T> =
	copy(matchMap = matchMap.update(bits.first) {
		forDefine.define(bits.remaining, match)
	})

fun <T> Function<T>.define(bits: Seq<Bit>, match: Match<T>): Function<T> =
	bits.seqNodeOrNull.ifNotNullOr(
		{ define(it, match) },
		{ this })

fun <T> Function<T>.undefine(bit: Bit) =
	copy(matchMap = matchMap.put(bit, null))

fun <T> Function<T>.invoke(bit: Bit, scope: Scope<T>) =
	matchMap
		.at(bit)!!.invoke(scope)

fun <T> booleanFunction() =
	emptyFunction<T>()
		.define("false".id.bitSeq, zero.bit.push.op<T>().match)
		.define("true".id.bitSeq, one.bit.push.op<T>().match)

package leo32

import leo.base.Seq
import leo.base.SeqNode
import leo.base.ifNotNullOr
import leo.base.seqNodeOrNull
import leo.binary.*

data class Function(
	val matchMap: Map1<Match>) {
	override fun toString() = code
}

val emptyFunction =
	Function(nullMap1())

fun function(match0: Match, match1: Match) =
	Function(nullMap1<Match>().put(zero.bit, match0).put(one.bit, match1))

fun Function.put(bit: Bit, match: Match) =
	Function(matchMap.put(bit, match))

fun Function.define(bit: Bit, match: Match) =
	copy(matchMap = matchMap.put(bit, match))

fun Function.define(bits: SeqNode<Bit>, match: Match): Function =
	copy(matchMap = matchMap.updateAt(bits.first) {
		forDefine.define(bits.remaining, match)
	})

fun Function.define(bits: Seq<Bit>, match: Match): Function =
	bits.seqNodeOrNull.ifNotNullOr(
		{ define(it, match) },
		{ this })

fun Function.undefine(bit: Bit) =
	copy(matchMap = matchMap.put(bit, null))

fun Function.invoke(bit: Bit, runtime: Runtime) =
	(matchMap.at(bit) ?: push.op.match).invoke(bit, runtime)

fun Function.matchAt(bit: Bit) =
	matchMap.at(bit) ?: push.op.match

val nandFunction =
	function(
		push.op.match.next(
			function(
				push.op.then(nand.op).match,
				push.op.then(nand.op).match)),
		push.op.match.next(
			function(
				push.op.then(nand.op).match,
				push.op.then(nand.op).match)))
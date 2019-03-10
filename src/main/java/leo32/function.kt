package leo32

import leo.base.SeqNode
import leo.binary.*

data class Function(
	val matchArray: Array1<Match>) {
	override fun toString() = code
}

val emptyFunction =
	Function(nullArray1())

fun Function.put(bit: Bit, match: Match) =
	Function(matchArray.put(bit, match))

fun Function.define(bit: Bit, match: Match) =
	copy(matchArray = matchArray.put(bit, match))

fun Function.define(bits: SeqNode<Bit>, match: Match): Function =
	copy(matchArray = matchArray.updateAt(bits.first) {
		notNull.define(bits.remaining, match)
	})

fun Function.undefine(bit: Bit) =
	copy(matchArray = matchArray.put(bit, null))

fun Function.invoke(bit: Bit, runtime: Runtime) =
	matchArray.at(bit)?.invoke(bit, runtime)

fun Function.matchAt(bit: Bit) =
	matchArray.at(bit)
		?: emptyFunction.partialMatch

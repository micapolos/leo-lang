package leo3

import leo.base.appendableString
import leo.base.empty
import leo.base.fold
import leo.binary.byteBitSeq
import leo32.*

data class Function(
	val tokenToMatchDict: Dict<Token, Match>) {
	override fun toString() = appendableString { it.append(this) }
}

fun function(vararg pairs: Pair<Token, Match>) =
	empty
		.dict<Token, Match> { byteSeq.byteBitSeq }
		.fold(pairs) { pair -> put(pair.first, pair.second) }
		.run { Function(this) }

fun Function.at(token: Token) =
	tokenToMatchDict.at(token)

val Function.isEmpty
	get() = tokenToMatchDict.isEmpty

fun Appendable.append(function: Function): Appendable =
	if (function.isEmpty) this
	else append('?')
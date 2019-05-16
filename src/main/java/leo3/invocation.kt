package leo3

import leo.base.appendableString
import leo.base.empty

data class Invocation(
	val tokenReader: TokenReader,
	val function: Function) {
	override fun toString() = appendableString { it.append(this) }
}

fun invocation(function: Function) =
	Invocation(empty.tokenReader, function)

fun invocation(termOrNull: Term?) =
	Invocation(empty.tokenReader.plus(termOrNull), function())

fun Invocation.plus(token: Token): Invocation? =
	function.at(token)?.let { matchAtToken ->
		tokenReader.plus(token)?.let { tokenReaderPlusToken ->
			matchAtToken.resolve(tokenReaderPlusToken)
		}
	}

fun Appendable.append(invocation: Invocation): Appendable =
	this
		.append(invocation.tokenReader)
		.append(invocation.function)
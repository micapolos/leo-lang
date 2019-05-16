package leo3

import leo.base.appendableString
import leo.base.empty

data class Value(
	val termParser: TermParser,
	val function: Function) {
	override fun toString() = appendableString { it.append(this) }
}

fun value(function: Function) =
	Value(empty.termParser, function)

fun value(termOrNull: Term?) =
	Value(empty.termParser.plus(termOrNull), function())

fun Value.plus(token: Token): Value? =
	function.at(token)?.let { matchAtToken ->
		termParser.plus(token)?.let { tokenReaderPlusToken ->
			matchAtToken.resolve(tokenReaderPlusToken)
		}
	}

fun Appendable.append(value: Value): Appendable =
	this
		.append(value.termParser)
		.append(value.function)
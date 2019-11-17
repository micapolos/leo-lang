package leo14.typed.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.typed.*

data class ChoiceParser<T>(
	val ender: ChoiceEnder<T>?,
	val dictionary: Dictionary,
	val choice: Choice)

data class ChoiceEnder<T>(
	val typeParser: TypeParser<T>)

fun <T> ChoiceParser<T>.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken -> null
		is BeginToken -> leo(TypeParser<T>(OptionTypeEnder(this, token.begin.string), null, dictionary, type()))
		is EndToken -> ender?.end(choice)
	} ?: error("$this.parse($token)")

fun <T> ChoiceEnder<T>.end(choice: Choice): Leo<T> =
	leo<T>(typeParser.plus(line(choice)))

fun <T> ChoiceParser<T>.plus(option: Option): ChoiceParser<T> =
	copy(choice = choice.plus(option))
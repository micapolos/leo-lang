package leo14.typed.compiler

import leo.base.notNullIf
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.typed.*

data class ChoiceParser(
	val interceptor: ChoiceInterceptor?,
	val dictionary: Dictionary,
	val choice: Choice)

data class ChoiceInterceptor(
	val typeParser: TypeParser)

fun <T> ChoiceParser.parse(token: Token): Leo<T> =
	interceptor
		?.end(choice, token)
		?: when (token) {
			is LiteralToken -> null
			is BeginToken -> leo(TypeParser(OptionTypeEnder(this, token.begin.string), null, dictionary, type()))
			is EndToken -> null
		}
		?: error("$this.parse($token)")

fun <T> ChoiceInterceptor.end(choice: Choice, token: Token): Leo<T>? =
	notNullIf(token is EndToken) {
		leo<T>(typeParser.plus<T>(line(choice)))
	}

fun ChoiceParser.plus(option: Option): ChoiceParser =
	copy(choice = choice.plus(option))
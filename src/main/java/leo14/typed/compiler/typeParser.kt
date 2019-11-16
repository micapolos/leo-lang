package leo14.typed.compiler

import leo.base.notNullIf
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.typed.*

data class TypeParser(
	val interceptor: TypeInterceptor?,
	val dictionary: Dictionary,
	val type: Type)

sealed class TypeInterceptor
data class LineTypeInterceptor(val typeParser: TypeParser, val name: String) : TypeInterceptor()
data class ArrowLhsTypeInterceptor(val typeParser: TypeParser) : TypeInterceptor()
data class ArrowRhsTypeInterceptor(val typeParser: TypeParser, val lhsType: Type) : TypeInterceptor()
data class OptionTypeInterceptor(val choiceParser: ChoiceParser, val name: String) : TypeInterceptor()

fun <T> TypeParser.parse(token: Token): Leo<T> =
	interceptor
		?.intercept(type, token)
		?: when (token) {
			is LiteralToken -> null
			is BeginToken ->
				when (token.begin.string) {
					dictionary.choice -> leo(ChoiceParser(ChoiceInterceptor(this), dictionary, choice()))
					dictionary.action -> leo(TypeParser(ArrowLhsTypeInterceptor(this), dictionary, type()))
					dictionary.native -> leo(NativeParser(this))
					else -> leo<T>(TypeParser(LineTypeInterceptor(this, token.begin.string), dictionary, type()))
				}
			is EndToken -> null
		}
		?: error("$this.parse($token)")

fun <T> TypeParser.plus(line: Line): TypeParser =
	copy(type = type.plus(line))

fun <T> TypeInterceptor.intercept(type: Type, token: Token): Leo<T>? =
	when (this) {
		is LineTypeInterceptor ->
			notNullIf(token is EndToken) {
				leo<T>(typeParser.plus<T>(name lineTo type))
			}
		is ArrowLhsTypeInterceptor ->
			notNullIf(token is BeginToken && token.begin.string == typeParser.dictionary.giving) {
				leo<T>(TypeParser(ArrowRhsTypeInterceptor(typeParser, type), typeParser.dictionary, type()))
			}
		is ArrowRhsTypeInterceptor ->
			notNullIf(token is EndToken) {
				leo<T>(ArrowParser(ArrowInterceptor(typeParser), lhsType arrowTo type))
			}
		is OptionTypeInterceptor ->
			notNullIf(token is EndToken) {
				leo<T>(choiceParser.plus(name optionTo type))
			}
	}

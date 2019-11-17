package leo14.typed.compiler

import leo14.*
import leo14.typed.*

data class TypeParser(
	val ender: TypeEnder?,
	val beginner: TypeBeginner?,
	val dictionary: Dictionary,
	val type: Type)

sealed class TypeEnder
data class LineTypeEnder(val typeParser: TypeParser, val name: String) : TypeEnder()
data class ArrowGivingTypeEnder(val typeParser: TypeParser, val lhsType: Type) : TypeEnder()
data class OptionTypeEnder(val choiceParser: ChoiceParser, val name: String) : TypeEnder()

sealed class TypeBeginner
data class ArrowGivingTypeBeginner(val typeParser: TypeParser) : TypeBeginner()

fun <T> TypeParser.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken -> null
		is BeginToken -> beginner
			?.begin(dictionary, type, token.begin)
			?: when (token.begin.string) {
				dictionary.choice -> leo(ChoiceParser(ChoiceInterceptor(this), dictionary, choice()))
				dictionary.action -> leo(TypeParser(null, ArrowGivingTypeBeginner(this), dictionary, type()))
				dictionary.native -> leo(NativeParser(this))
				else -> leo(TypeParser(LineTypeEnder(this, token.begin.string), null, dictionary, type()))
			}
		is EndToken -> ender?.end(type)
	} ?: error("$this.parse($token)")

fun <T> TypeParser.plus(line: Line): TypeParser =
	copy(type = type.plus(line))

fun <T> TypeBeginner.begin(dictionary: Dictionary, type: Type, begin: Begin): Leo<T>? =
	when (this) {
		is ArrowGivingTypeBeginner ->
			when (begin.string) {
				dictionary.giving -> leo(TypeParser(ArrowGivingTypeEnder(typeParser, type), null, typeParser.dictionary, type()))
				else -> null
			}
	}

fun <T> TypeEnder.end(type: Type): Leo<T>? =
	when (this) {
		is LineTypeEnder ->
			leo(typeParser.plus<T>(name lineTo type))
		is ArrowGivingTypeEnder ->
			leo(ArrowParser(ArrowEnder(typeParser), lhsType arrowTo type))
		is OptionTypeEnder ->
			leo(choiceParser.plus(name optionTo type))
	}

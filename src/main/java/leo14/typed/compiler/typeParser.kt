package leo14.typed.compiler

import leo14.*
import leo14.typed.*

data class TypeParser<T>(
	val ender: TypeEnder<T>?,
	val beginner: TypeBeginner<T>?,
	val dictionary: Dictionary,
	val type: Type)

sealed class TypeEnder<T>
data class LineTypeEnder<T>(val typeParser: TypeParser<T>, val name: String) : TypeEnder<T>()
data class ArrowGivingTypeEnder<T>(val typeParser: TypeParser<T>, val lhsType: Type) : TypeEnder<T>()
data class OptionTypeEnder<T>(val choiceParser: ChoiceParser<T>, val name: String) : TypeEnder<T>()
data class AsTypeEnder<T>(val compiledParser: CompiledParser<T>) : TypeEnder<T>()

sealed class TypeBeginner<T>
data class ActionDoesTypeBeginner<T>(val compiledParser: CompiledParser<T>) : TypeBeginner<T>()
data class ArrowGivingTypeBeginner<T>(val typeParser: TypeParser<T>) : TypeBeginner<T>()

fun <T> TypeParser<T>.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken -> null
		is BeginToken -> beginner
			?.begin(dictionary, type, token.begin)
			?: when (token.begin.string) {
				dictionary.choice -> leo(ChoiceParser(ChoiceEnder(this), dictionary, choice()))
				dictionary.action -> leo(TypeParser(null, ArrowGivingTypeBeginner(this), dictionary, type()))
				dictionary.native -> leo(NativeParser(this))
				else -> leo(TypeParser(LineTypeEnder(this, token.begin.string), null, dictionary, type()))
			}
		is EndToken -> ender?.end(type)
	} ?: error("$this.parse($token)")

fun <T> TypeParser<T>.plus(line: Line): TypeParser<T> =
	copy(type = type.plus(line))

fun <T> TypeBeginner<T>.begin(dictionary: Dictionary, type: Type, begin: Begin): Leo<T>? =
	when (this) {
		is ArrowGivingTypeBeginner ->
			when (begin.string) {
				dictionary.giving ->
					leo(
						TypeParser(
							ArrowGivingTypeEnder(typeParser, type),
							null,
							typeParser.dictionary,
							type()))
				else -> null
			}
		is ActionDoesTypeBeginner ->
			when (begin.string) {
				dictionary.does ->
					leo(
						CompiledParser(
							ActionDoesEnder(compiledParser, type),
							compiledParser.context,
							compiledParser.compiled.beginDoes(type)))
				else -> null
			}
	}

fun <T> TypeEnder<T>.end(type: Type): Leo<T>? =
	when (this) {
		is LineTypeEnder ->
			leo(typeParser.plus(name lineTo type))
		is ArrowGivingTypeEnder ->
			leo(ArrowParser(ArrowEnder(typeParser), lhsType arrowTo type))
		is OptionTypeEnder ->
			leo(choiceParser.plus(name optionTo type))
		is AsTypeEnder ->
			leo(compiledParser.updateCompiled { updateTyped { `as`(type) } })
	}

package leo14.typed.compiler

import leo14.*
import leo14.typed.*

data class TypeParser<T>(
	val parent: TypeParserParent<T>?,
	val beginner: TypeBeginner<T>?,
	val dictionary: Dictionary,
	val type: Type)

sealed class TypeParserParent<T>
data class LineTypeParserParent<T>(val typeParser: TypeParser<T>, val name: String) : TypeParserParent<T>()
data class ArrowGivingTypeParserParent<T>(val typeParser: TypeParser<T>, val lhsType: Type) : TypeParserParent<T>()
data class OptionTypeParserParent<T>(val choiceParser: ChoiceParser<T>, val name: String) : TypeParserParent<T>()
data class AsTypeParserParent<T>(val compiledParser: CompiledParser<T>) : TypeParserParent<T>()
data class ForgetTypeParserParent<T>(val compiledParser: CompiledParser<T>) : TypeParserParent<T>()

sealed class TypeBeginner<T>
data class ActionDoesTypeBeginner<T>(val compiledParser: CompiledParser<T>) : TypeBeginner<T>()
data class ArrowGivingTypeBeginner<T>(val typeParser: TypeParser<T>) : TypeBeginner<T>()
data class RememberTypeBeginner<T>(val compiledParser: CompiledParser<T>) : TypeBeginner<T>()

fun <T> TypeParser<T>.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken -> null
		is BeginToken -> beginner
			?.begin(dictionary, type, token.begin)
			?: when (token.begin.string) {
				dictionary.choice -> leo(ChoiceParser(ChoiceParserParent(this), dictionary, choice()))
				dictionary.action -> leo(TypeParser(null, ArrowGivingTypeBeginner(this), dictionary, type()))
				dictionary.native -> leo(NativeParser(this))
				else -> leo(TypeParser(LineTypeParserParent(this, token.begin.string), null, dictionary, type()))
			}
		is EndToken -> parent?.end(type)
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
							ArrowGivingTypeParserParent(typeParser, type),
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
							ActionDoesParserParent(compiledParser, type),
							null,
							compiledParser.context,
							Phase.COMPILER,
							compiledParser.compiled.beginDoes(type)))
				else -> null
			}
		is RememberTypeBeginner ->
			when (begin.string) {
				dictionary.`is` ->
					leo(
						CompiledParser(
							RememberIsParserParent(compiledParser, type),
							null,
							compiledParser.context,
							compiledParser.phase,
							compiledParser.compiled.begin))
				dictionary.does ->
					leo(
						CompiledParser(
							RememberDoesParserParent(compiledParser, type),
							null,
							compiledParser.context,
							Phase.COMPILER,
							compiledParser.compiled.beginDoes(type)))
				else -> null
			}
	}

fun <T> TypeParserParent<T>.end(type: Type): Leo<T>? =
	when (this) {
		is LineTypeParserParent ->
			leo(typeParser.plus(name lineTo type))
		is ArrowGivingTypeParserParent ->
			leo(ArrowParser(ArrowParserParent(typeParser), lhsType arrowTo type))
		is OptionTypeParserParent ->
			leo(choiceParser.plus(name optionTo type))
		is AsTypeParserParent ->
			compiledParser.next { updateTyped { `as`(type) } }
		is ForgetTypeParserParent ->
			compiledParser.next { updateMemory { plus(forget(type)) } }
	}


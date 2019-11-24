package leo14.typed.compiler

import leo14.*
import leo14.typed.*

data class ChoiceParser<T>(
	val parent: ChoiceParserParent<T>?,
	val dictionary: Dictionary,
	val choice: Choice)

data class ChoiceParserParent<T>(
	val typeParser: TypeParser<T>)

fun <T> ChoiceParser<T>.parse(token: Token): Compiler<T> =
	when (token) {
		is LiteralToken -> null
		is BeginToken -> compiler(TypeParser<T>(OptionTypeParserParent(this, token.begin.string), null, dictionary, type()))
		is EndToken -> parent?.end(choice)
	} ?: error("$this.parse($token)")

fun <T> ChoiceParserParent<T>.end(choice: Choice): Compiler<T> =
	compiler<T>(typeParser.plus(line(choice)))

fun <T> ChoiceParser<T>.plus(option: Option): ChoiceParser<T> =
	copy(choice = choice.plus(option))
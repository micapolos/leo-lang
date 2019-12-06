package leo14.typed.compiler

import leo14.*
import leo14.typed.*

data class TypeParser<T>(
	val parent: TypeParserParent<T>?,
	val beginner: TypeBeginner<T>?,
	val language: Language,
	val typeContext: TypeContext,
	val type: Type)

fun <T> TypeParser<T>.parse(token: Token): Compiler<T> =
	when (token) {
		is LiteralToken -> null
		is BeginToken -> beginner
			?.begin(language, type, token.begin)
			?: when (token.begin.string) {
				Keyword.CHOICE stringIn language ->
					compiler(
						ChoiceParser(
							ChoiceParserParent(this),
							language,
							typeContext,
							choice()))
				Keyword.FUNCTION stringIn language ->
					compiler(
						TypeParser(
							null,
							ArrowGivingTypeBeginner(this),
							language,
							typeContext,
							type()))
				else ->
					compiler(
						TypeParser(
							LineTypeParserParent(this, token.begin.string),
							null,
							language,
							typeContext,
							type()))
			}
		is EndToken -> parent?.end(type)
	} ?: error("$this.parse(token)")

fun <T> TypeParser<T>.plus(line: Line): TypeParser<T> =
	copy(type = type.plus(typeContext.resolve(line)))

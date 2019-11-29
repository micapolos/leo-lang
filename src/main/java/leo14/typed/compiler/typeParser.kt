package leo14.typed.compiler

import leo14.*
import leo14.typed.*

data class TypeParser<T>(
	val parent: TypeParserParent<T>?,
	val beginner: TypeBeginner<T>?,
	val language: Language,
	val typeContext: TypeContext,
	val type: Type)

sealed class TypeParserParent<T>
data class LineTypeParserParent<T>(val typeParser: TypeParser<T>, val name: String) : TypeParserParent<T>()
data class ArrowGivingTypeParserParent<T>(val typeParser: TypeParser<T>, val lhsType: Type) : TypeParserParent<T>()
data class OptionTypeParserParent<T>(val choiceParser: ChoiceParser<T>, val name: String) : TypeParserParent<T>()
data class AsTypeParserParent<T>(val compiledParser: CompiledParser<T>) : TypeParserParent<T>()
data class ForgetTypeParserParent<T>(val compiledParser: CompiledParser<T>) : TypeParserParent<T>()

sealed class TypeBeginner<T>
data class FunctionGivesTypeBeginner<T>(val compiledParser: CompiledParser<T>) : TypeBeginner<T>()
data class ArrowGivingTypeBeginner<T>(val typeParser: TypeParser<T>) : TypeBeginner<T>()
data class RememberTypeBeginner<T>(val compiledParser: CompiledParser<T>) : TypeBeginner<T>()
data class ForgetTypeBeginner<T>(val compiledParser: CompiledParser<T>) : TypeBeginner<T>()

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
	} ?: error("$this.parse($token)")

fun <T> TypeParser<T>.plus(line: Line): TypeParser<T> =
	copy(type = type.plus(typeContext.resolve(line)))

fun <T> TypeBeginner<T>.begin(language: Language, type: Type, begin: Begin): Compiler<T>? =
	when (this) {
		is ArrowGivingTypeBeginner ->
			when (begin.string) {
				Keyword.GIVING stringIn language ->
					compiler(
						TypeParser(
							ArrowGivingTypeParserParent(typeParser, type),
							null,
							typeParser.language,
							typeParser.typeContext,
							type()))
				else -> null
			}
		is FunctionGivesTypeBeginner ->
			when (begin.string) {
				Keyword.DOES stringIn language ->
					compiler(
						CompiledParser(
							FunctionDoesParserParent(compiledParser, type),
							compiledParser.context,
							Phase.COMPILER,
							compiledParser.compiled.beginGives(type)))
				else -> null
			}
		is RememberTypeBeginner ->
			when (begin.string) {
				Keyword.IS stringIn language ->
					compiler(
						CompiledParser(
							RememberIsParserParent(compiledParser, type),
							compiledParser.context,
							compiledParser.phase,
							compiledParser.compiled.begin))
				Keyword.DOES stringIn language ->
					compiler(
						CompiledParser(
							RememberDoesParserParent(compiledParser, type),
							compiledParser.context,
							Phase.COMPILER,
							compiledParser.compiled.beginGives(type)))
				else -> null
			}
		is ForgetTypeBeginner ->
			when (begin.string) {
				Keyword.EVERYTHING stringIn language -> ForgetEverythingParserCompiler(ForgetEverythingParser(compiledParser))
				else -> null
			}
	}

fun <T> TypeParserParent<T>.end(type: Type): Compiler<T>? =
	when (this) {
		is LineTypeParserParent ->
			compiler(typeParser.plus(name lineTo type))
		is ArrowGivingTypeParserParent ->
			compiler(ArrowParser(ArrowParserParent(typeParser), lhsType arrowTo type))
		is OptionTypeParserParent ->
			compiler(choiceParser.plus(name optionTo type))
		is AsTypeParserParent ->
			compiledParser.next { updateTyped { `as`(type) } }
		is ForgetTypeParserParent ->
			compiledParser.next { updateMemory { forget(type) } }
	}


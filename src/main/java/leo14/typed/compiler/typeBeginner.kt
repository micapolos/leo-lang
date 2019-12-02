package leo14.typed.compiler

import leo14.Begin
import leo14.Keyword
import leo14.Language
import leo14.keywordOrNullIn
import leo14.typed.Type

sealed class TypeBeginner<T>

data class FunctionGivesTypeBeginner<T>(val compiledParser: CompiledParser<T>) : TypeBeginner<T>()
data class ArrowGivingTypeBeginner<T>(val typeParser: TypeParser<T>) : TypeBeginner<T>()
data class RememberTypeBeginner<T>(val compiledParser: CompiledParser<T>) : TypeBeginner<T>()
data class ForgetTypeBeginner<T>(val compiledParser: CompiledParser<T>) : TypeBeginner<T>()

fun <T> TypeBeginner<T>.begin(language: Language, type: Type, begin: Begin): Compiler<T>? =
	when (this) {
		is ArrowGivingTypeBeginner ->
			when (begin.string keywordOrNullIn language) {
				Keyword.GIVING ->
					compiler(
						TypeParser(
							ArrowGivingTypeParserParent(typeParser, type),
							null,
							typeParser.language,
							typeParser.typeContext,
							leo14.typed.type()))
				else -> null
			}
		is FunctionGivesTypeBeginner ->
			when (begin.string keywordOrNullIn language) {
				Keyword.DOES ->
					compiler(
						compiledParser
							.begin(FunctionDoesParserParent(compiledParser, type), CompilerKind.COMPILER)
							.updateCompiled { plusGiven(type) })
				else -> null
			}
		is RememberTypeBeginner ->
			when (begin.string keywordOrNullIn language) {
				Keyword.IS ->
					compiler(compiledParser.begin(RememberIsParserParent(compiledParser, type)))
				Keyword.DOES ->
					compiler(
						compiledParser
							.begin(RememberDoesParserParent(compiledParser, type), CompilerKind.COMPILER)
							.updateCompiled { plusGiven(type) })
				else -> null
			}
		is ForgetTypeBeginner ->
			when (begin.string keywordOrNullIn language) {
				Keyword.EVERYTHING -> ForgetEverythingParserCompiler(ForgetEverythingParser(compiledParser))
				else -> null
			}
	}

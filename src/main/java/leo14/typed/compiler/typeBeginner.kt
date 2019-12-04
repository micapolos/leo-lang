package leo14.typed.compiler

import leo14.*
import leo14.typed.Type
import leo14.typed.type

sealed class TypeBeginner<T>

data class FunctionGivesTypeBeginner<T>(val compiledParser: CompiledParser<T>) : TypeBeginner<T>()
data class ArrowGivingTypeBeginner<T>(val typeParser: TypeParser<T>) : TypeBeginner<T>()
data class DefineTypeBeginner<T>(val defineParser: DefineParser<T>) : TypeBeginner<T>()

fun <T> TypeBeginner<T>.begin(language: Language, type: Type, begin: Begin): Compiler<T>? =
	when (this) {
		is ArrowGivingTypeBeginner ->
			when (begin.string keywordOrNullIn language) {
				Keyword.GIVING ->
					compiler(
						TypeParser(
							ArrowGivesTypeParserParent(typeParser, type),
							null,
							typeParser.language,
							typeParser.typeContext,
							type()))
				else -> null
			}
		is FunctionGivesTypeBeginner ->
			when (begin.string keywordOrNullIn language) {
				Keyword.GIVES ->
					compiler(
						compiledParser
							.begin(FunctionDoesParserParent(compiledParser, type), CompilerKind.COMPILER)
							.updateCompiled {
								plusGiven(
									Keyword.GIVEN stringIn compiledParser.context.language,
									type)
							})
				else -> null
			}
		is DefineTypeBeginner ->
			when (begin.string keywordOrNullIn language) {
				Keyword.IS ->
					compiler(defineParser.beginIs(type))
				Keyword.GIVES ->
					compiler(defineParser.beginGives(type))
				else -> null
			}
	}

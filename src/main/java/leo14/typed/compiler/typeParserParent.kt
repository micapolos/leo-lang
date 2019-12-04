package leo14.typed.compiler

import leo14.typed.*

sealed class TypeParserParent<T>

data class LineTypeParserParent<T>(val typeParser: TypeParser<T>, val name: String) : TypeParserParent<T>()
data class ArrowGivesTypeParserParent<T>(val typeParser: TypeParser<T>, val lhsType: Type) : TypeParserParent<T>()
data class OptionTypeParserParent<T>(val choiceParser: ChoiceParser<T>, val name: String) : TypeParserParent<T>()
data class AsTypeParserParent<T>(val compiledParser: CompiledParser<T>) : TypeParserParent<T>()

fun <T> TypeParserParent<T>.end(type: Type): Compiler<T>? =
	when (this) {
		is LineTypeParserParent ->
			compiler(typeParser.plus(name lineTo type))
		is ArrowGivesTypeParserParent ->
			compiler(ArrowParser(ArrowParserParent(typeParser), lhsType arrowTo type))
		is OptionTypeParserParent ->
			compiler(choiceParser.plus(name optionTo type))
		is AsTypeParserParent ->
			compiledParser.nextCompiler { updateTyped { `as`(type) } }
	}


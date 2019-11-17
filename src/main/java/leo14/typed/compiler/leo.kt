package leo14.typed.compiler

import leo14.*
import leo14.native.Native
import leo14.typed.Type

sealed class Leo<T>

data class TypeParserLeo<T>(val typeParser: TypeParser<T>) : Leo<T>()
data class ChoiceParserLeo<T>(val choiceParser: ChoiceParser<T>) : Leo<T>()
data class ArrowParserLeo<T>(val arrowParser: ArrowParser<T>) : Leo<T>()
data class NativeParserLeo<T>(val nativeParser: NativeParser<T>) : Leo<T>()
data class CompiledParserLeo<T>(val compiledParser: CompiledParser<T>) : Leo<T>()
data class ActionParserLeo<T>(val actionParser: ActionParser<T>) : Leo<T>()

fun <T> leo(typeParser: TypeParser<T>): Leo<T> = TypeParserLeo(typeParser)
fun <T> leo(choiceParser: ChoiceParser<T>): Leo<T> = ChoiceParserLeo(choiceParser)
fun <T> leo(arrowParser: ArrowParser<T>): Leo<T> = ArrowParserLeo(arrowParser)
fun <T> leo(nativeParser: NativeParser<T>): Leo<T> = NativeParserLeo(nativeParser)
fun <T> leo(compiledParser: CompiledParser<T>): Leo<T> = CompiledParserLeo(compiledParser)
fun <T> leo(actionParser: ActionParser<T>): Leo<T> = ActionParserLeo(actionParser)

fun leo(type: Type): Leo<Native> =
	leo(TypeParser(null, null, englishDictionary, type))

fun leo(compiled: Compiled<Native>): Leo<Native> =
	CompiledParserLeo(CompiledParser(null, nativeContext, compiled))

fun <T> Leo<T>.parse(token: Token): Leo<T> =
	when (this) {
		is TypeParserLeo -> typeParser.parse(token)
		is ChoiceParserLeo -> choiceParser.parse(token)
		is ArrowParserLeo -> arrowParser.parse(token)
		is NativeParserLeo -> nativeParser.parse(token)
		is CompiledParserLeo -> compiledParser.parse(token)
		is ActionParserLeo -> actionParser.parse(token)
	}

fun <T> Leo<T>.parse(script: Script): Leo<T> =
	when (script) {
		is UnitScript -> this
		is LinkScript -> parse(script.link)
	}

fun <T> Leo<T>.parse(link: ScriptLink): Leo<T> =
	parse(link.lhs).parse(link.line)

fun <T> Leo<T>.parse(line: ScriptLine): Leo<T> =
	when (line) {
		is LiteralScriptLine -> parse(token(line.literal))
		is FieldScriptLine -> parse(line.field)
	}

fun <T> Leo<T>.parse(field: ScriptField): Leo<T> =
	this
		.parse(token(begin(field.string)))
		.parse(field.rhs)
		.parse(token(end))

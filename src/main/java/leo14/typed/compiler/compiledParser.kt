package leo14.typed.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.typed.*

data class CompiledParser<T>(
	val ender: CompiledEnder<T>?,
	val context: Context<T>,
	val compiled: Compiled<T>)

sealed class CompiledEnder<T>
data class FieldCompiledEnder<T>(val compiledParser: CompiledParser<T>, val name: String) : CompiledEnder<T>()
data class ActionDoesEnder<T>(val compiledParser: CompiledParser<T>, val type: Type) : CompiledEnder<T>()

fun <T> CompiledParser<T>.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken ->
			CompiledParserLeo(resolve(context.compileLine(token.literal)))
		is BeginToken ->
			when (token.begin.string) {
				context.dictionary.action ->
					leo(TypeParser(null, ActionDoesTypeBeginner(this), context.dictionary, type()))
				context.dictionary.`as` ->
					leo(TypeParser(AsTypeEnder(this), null, context.dictionary, type()))
				else ->
					CompiledParserLeo(
						CompiledParser(
							FieldCompiledEnder(this, token.begin.string),
							context,
							compiled.begin))
			}
		is EndToken -> ender?.end(compiled)
	} ?: error("$this.parse($token)")

fun <T> CompiledEnder<T>.end(compiled: Compiled<T>): Leo<T> =
	when (this) {
		is FieldCompiledEnder ->
			leo(compiledParser.resolve(line(name fieldTo compiled.typed)))
		is ActionDoesEnder ->
			leo(ActionParser(compiledParser, type does compiled.typed))

	}

fun <T> CompiledParser<T>.resolve(line: TypedLine<T>) =
	copy(compiled = compiled.resolve(line))

fun <T> CompiledParser<T>.updateCompiled(fn: Compiled<T>.() -> Compiled<T>) =
	copy(compiled = compiled.fn())

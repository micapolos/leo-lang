package leo14.typed.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.typed.TypedLine
import leo14.typed.fieldTo
import leo14.typed.line
import leo14.typed.type

data class CompiledParser<T>(
	val ender: CompiledEnder<T>?,
	val context: Context<T>,
	val compiled: Compiled<T>)

sealed class CompiledEnder<T>
data class FieldCompiledEnder<T>(val compiledParser: CompiledParser<T>, val name: String) : CompiledEnder<T>()

fun <T> CompiledParser<T>.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken ->
			CompiledParserLeo(plus(context.compileLine(token.literal)))
		is BeginToken ->
			when (token.begin.string) {
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
			CompiledParserLeo(compiledParser.plus(line(name fieldTo compiled.typed)))
	}

fun <T> CompiledParser<T>.plus(line: TypedLine<T>) =
	copy(compiled = compiled.plus(line))

fun <T> CompiledParser<T>.updateCompiled(fn: Compiled<T>.() -> Compiled<T>) =
	copy(compiled = compiled.fn())

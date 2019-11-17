package leo14.typed.compiler

import leo.base.notNullIf
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.typed.TypedLine
import leo14.typed.fieldTo
import leo14.typed.line

data class CompiledParser<T>(
	val interceptor: CompiledInterceptor<T>?,
	val context: Context<T>,
	val compiled: Compiled<T>)

sealed class CompiledInterceptor<T>
data class FieldCompiledInterceptor<T>(val compiledParser: CompiledParser<T>, val name: String) : CompiledInterceptor<T>()

fun <T> CompiledParser<T>.parse(token: Token): Leo<T> =
	interceptor
		?.end(token, compiled)
		?: when (token) {
			is LiteralToken ->
				CompiledParserLeo(plus(context.compileLine(token.literal)))
			is BeginToken ->
				when (token.begin.string) {
					else ->
						CompiledParserLeo(
							CompiledParser(
								FieldCompiledInterceptor(this, token.begin.string),
								context,
								compiled.begin))
				}
			is EndToken -> null
		}
		?: error("$this.parse($token)")

fun <T> CompiledInterceptor<T>.end(token: Token, compiled: Compiled<T>): Leo<T>? =
	when (this) {
		is FieldCompiledInterceptor ->
			notNullIf(token is EndToken) {
				CompiledParserLeo(compiledParser.plus(line(name fieldTo compiled.typed)))
			}
	}

fun <T> CompiledParser<T>.plus(line: TypedLine<T>) =
	copy(compiled = compiled.plus(line))
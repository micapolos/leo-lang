package leo21.token.repl

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.lambda.value.Scope
import leo14.lambda.value.Value
import leo21.prim.Prim
import leo21.token.processor.Processor
import leo21.token.processor.plus

data class Repl(
	val parentOrNull: Parent?,
	val processor: Processor,
	val context: Context
) {
	data class Parent(val repl: Repl, val name: String)
	data class Context(val scope: Scope<Prim>, val valueOrNull: Value<Prim>?)
}

fun Repl.plus(token: Token): Repl =
	when (token) {
		is LiteralToken ->
			Repl(
				parentOrNull,
				processor.plus(token),
				context)
		is BeginToken ->
			when (token.begin.string) {
				"do" -> TODO()
				else -> Repl(
					Repl.Parent(this, token.begin.string),
					processor.plus(token),
					context)
			}
		is EndToken -> parentOrNull!!.plus(processor.plus(token), context)
	}

fun Repl.Parent.plus(processor: Processor, context: Repl.Context): Repl =
	TODO()
package leo14.typed.interpreter

import leo14.*
import leo14.lambda.Scope
import leo14.typed.Typed
import leo14.typed.compiler.Context
import leo14.typed.compiler.TypedCompiler
import leo14.typed.compiler.compile

data class Interpreter<T>(
	val parent: InterpreterParent<T>?,
	val context: Context<T>,
	val scope: Scope<T>,
	val typed: Typed<T>)

fun <T> Interpreter<T>.interpret(token: Token): Interpreter<T> =
	when (token) {
		is LiteralToken ->
			TypedCompiler(null, context, typed)
				.compile(token)
				.let { compilerWithLiteral ->
					when (compilerWithLiteral) {
						is TypedCompiler -> copy(
							typed = compilerWithLiteral.typed,
							context = compilerWithLiteral.context)
						else -> TODO()
					}
				}
		is BeginToken ->
			TypedCompiler(null, context, typed)
				.compile(token)
				.let { beganCompiler ->
					when (beganCompiler) {
						is TypedCompiler ->
							Interpreter(
								InterpreterParent(this, token.begin),
								beganCompiler.context,
								scope,
								beganCompiler.typed)
						else -> TODO()
					}
				}
		is EndToken ->
			parent!!.plus(typed)
	}

data class InterpreterParent<T>(val interpreter: Interpreter<T>?, val begin: Begin)

fun <T> InterpreterParent<T>.plus(typed: Typed<T>): Interpreter<T> =
	TODO()
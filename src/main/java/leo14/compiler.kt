package leo14

import leo.base.orIfNull
import leo13.*
import leo14.js.compiler.Choice
import leo14.js.compiler.Fallback
import leo14.js.compiler.compile
import leo14.js.compiler.fallback

typealias Ret<T> = (T) -> Compiler
typealias Compile<T> = (Ret<T>) -> Compiler

interface Compiler {
	fun write(token: Token): Compiler
}

data class ResultCompiler<T>(val result: T) : Compiler {
	override fun write(token: Token) = error("eof")
}

fun <T> resultCompiler(result: T) = ResultCompiler(result)

fun <T> ret(): (T) -> Compiler = { resultCompiler(it) }

fun <T> Compiler.result(): T =
	(this as ResultCompiler<T>).result

fun compiler(write: (Token) -> Compiler): Compiler =
	object : Compiler {
		override fun write(token: Token) = write(token)
	}

val compileNothing: Compile<Nothing> = { compiler { error("nothing") } }

val eofCompiler = object : Compiler {
	override fun write(token: Token) = error("eof")
}

fun errorCompiler(string: String) =
	compiler { error(string) }

fun <T> compileError(string: String): Compile<T> = { errorCompiler(string) }

fun Compiler.write(string: String, writeRhs: Compiler.() -> Compiler) =
	this
		.write(token(begin(string)))
		.writeRhs()
		.write(token(end))

fun Compiler.writeNull() =
	write("null") { this }

fun Compiler.write(number: Number) =
	write(token(number))

fun Compiler.write(string: String) =
	write(token(string))

fun Compiler.write(script: Script): Compiler =
	when (script) {
		is UnitScript -> this
		is LinkScript -> write(script.link)
	}

fun Compiler.writeEnd(script: Script): Compiler =
	write(script).write(token(end))

fun <T> Compiler.compile(script: Script): T =
	tracing.write(script).write(token(end)).result()

fun <T> Compiler.compile(scriptLine: ScriptLine): T =
	tracing.write(scriptLine).result()

fun Compiler.write(link: ScriptLink) =
	write(link.lhs).write(link.line)

fun Compiler.write(line: ScriptLine) =
	when (line) {
		is LiteralScriptLine -> write(line.literal)
		is FieldScriptLine -> write(line.field)
	}

fun Compiler.write(literal: Literal) =
	when (literal) {
		is StringLiteral -> write(literal.string)
		is NumberLiteral -> write(literal.number)
	}

fun Compiler.write(field: ScriptField) =
	write(field.string) {
		write(field.rhs)
	}

fun <T> Compile<T>.compile(fn: Compiler.() -> Compiler): T =
	fn(
		invoke { ret ->
			resultCompiler(ret)
		}
	).let { compiler ->
		if (compiler is ResultCompiler<*>) compiler.result as T
		else error("not yet compiled")
	}

fun compiler(expectedToken: Token, fn: () -> Compiler): Compiler =
	object : Compiler {
		override fun write(token: Token) =
			if (token == expectedToken) fn()
			else error("expected: $expectedToken")
	}

fun stringCompiler(fn: (String) -> Compiler): Compiler =
	object : Compiler {
		override fun write(token: Token) =
			if (token is LiteralToken) fn((token.literal as StringLiteral).string)
			else error("$token is not a string")
	}

fun beginCompiler(string: String, ret: () -> Compiler): Compiler =
	compiler(token(begin(string)), ret)

fun endCompiler(ret: () -> Compiler): Compiler =
	compiler(token(end), ret)

fun switchCompiler(choice: Choice, vararg choices: Choice): Compiler =
	stack(choice, *choices).let { stack ->
		switchCompiler(
			fallback(
				compiler { token ->
					error("$token is not one of: ${stack.toList().joinToString { it.string }}")
				}
			),
			*stack.array)
	}

fun switchCompiler(fallback: Fallback, vararg choices: Choice): Compiler =
	stack(*choices).let { stack ->
		object : Compiler {
			override fun write(token: Token): Compiler =
				if (token is BeginToken)
					stack
						.mapFirst { compile(token.begin.string) }
						.orIfNull { fallback.compiler.write(token) }
				else error("$token is not field")
		}
	}

fun recursive(fn: () -> Compiler): Compiler =
	compiler { token ->
		fn().write(token)
	}

val Compiler.tracing get() = tracingIn(stack())

fun Compiler.tracingIn(tokenStack: Stack<Token>): Compiler =
	compiler { token ->
		try {
			write(token).run {
				if (this is ResultCompiler<*>) this
				else tracingIn(tokenStack.push(token))
			}
		} catch (ise: IllegalStateException) {
			error("${tokenStack.toList().joinToString("")}, got: $token, error: ${ise.message}")
		}
	}
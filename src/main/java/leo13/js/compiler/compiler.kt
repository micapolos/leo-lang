package leo13.js.compiler

import leo.base.orIfNull
import leo13.mapFirst
import leo13.stack
import leo13.toList

typealias Compile<T> = ((T) -> Compiler) -> Compiler

interface Compiler {
	fun write(token: Token): Compiler
}

fun compiler(write: (Token) -> Compiler): Compiler =
	object : Compiler {
		override fun write(token: Token) = write(token)
	}

val compileNothing: Compile<Nothing> = { compiler { error("nothing") } }

val eofCompiler = object : Compiler {
	override fun write(token: Token) = error("eof")
}

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

fun Compiler.write(link: ScriptLink) =
	write(link.lhs).write(link.line)

fun Compiler.write(line: ScriptLine) =
	when (line) {
		is StringScriptLine -> write(line.string)
		is NumberScriptLine -> write(line.number)
		is FieldScriptLine -> write(line.field)
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
			else error("$token expected")
	}

fun stringCompiler(fn: (String) -> Compiler): Compiler =
	object : Compiler {
		override fun write(token: Token) =
			if (token is StringToken) fn(token.string)
			else error("$token is not a string")
	}

fun numberCompiler(fn: (Number) -> Compiler): Compiler =
	object : Compiler {
		override fun write(token: Token) =
			if (token is NumberToken) fn(token.number)
			else error("$token is not a number")
	}

fun beginCompiler(string: String, ret: () -> Compiler): Compiler =
	compiler(token(begin(string)), ret)

fun endCompiler(ret: () -> Compiler): Compiler =
	compiler(token(end), ret)

fun switchCompiler(choice: Choice, vararg choices: Choice): Compiler =
	stack(choice, *choices).let { stack ->
		object : Compiler {
			override fun write(token: Token): Compiler =
				if (token is BeginToken)
					stack
						.mapFirst { compile(token.begin.string) }
						.orIfNull { error("$token is none of: ${stack.toList().joinToString { it.string }}") }
				else error("$token is not field")
		}
	}

fun switchCompiler(fallback: Fallback, vararg choices: Choice): Compiler =
	stack(*choices).let { stack ->
		object : Compiler {
			override fun write(token: Token): Compiler =
				if (token is BeginToken)
					stack
						.mapFirst { compile(token.begin.string) }
						.orIfNull { fallback.compiler }
				else error("$token is not field")
		}
	}

fun recursive(fn: () -> Compiler): Compiler =
	compiler { token ->
		fn().write(token)
	}

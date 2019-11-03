package leo13.js.compiler

import leo13.mapFirst
import leo13.stack

typealias Compile<T> = ((T) -> Compiler) -> Compiler

interface Compiler {
	fun write(token: Token): Compiler
}

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
	(fn(invoke { ret ->
		resultCompiler(ret)
	}) as ResultCompiler<T>).result

fun compiler(expectedToken: Token, fn: () -> Compiler): Compiler =
	object : Compiler {
		override fun write(token: Token) =
			if (token == expectedToken) fn()
			else error("$token expected")
	}

fun stringCompiler(fn: (String) -> Compiler): Compiler =
	object : Compiler {
		override fun write(token: Token) =
			(token as StringToken).run { fn(string) }
	}

fun numberCompiler(fn: (Number) -> Compiler): Compiler =
	object : Compiler {
		override fun write(token: Token) =
			(token as NumberToken).run { fn(number) }
	}

fun beginCompiler(string: String, ret: () -> Compiler): Compiler =
	compiler(token(begin(string)), ret)

fun endCompiler(ret: () -> Compiler): Compiler =
	compiler(token(end), ret)

fun compiler(choice: Choice, vararg choices: Choice): Compiler =
	stack(choice, *choices).let { stack ->
		object : Compiler {
			override fun write(token: Token): Compiler =
				(token as BeginToken).let { token ->
					stack.mapFirst {
						compile(token.begin.string)
					}!!
				}
		}
	}

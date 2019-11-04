package leo14.typed

import leo.base.notNullIf
import leo13.Stack
import leo13.mapFirst
import leo14.Compiler
import leo14.Number
import leo14.Ret
import leo14.lambda.Term

data class Context<T>(
	val entryStack: Stack<Entry<T>>,
	val compileString: (String) -> Term<T>,
	val compileNumber: (Number) -> Term<T>)

data class Entry<T>(val string: String, val compile: (Compiled<T>, Ret<Compiled<T>>) -> Compiler)

fun <T> Context<T>.compiler(string: String, typed: Typed<T>, ret: Ret<Compiled<T>>): Compiler =
	entryStack
		.mapFirst { compiler(string, Compiled(this@compiler, typed), ret) }
		?: compiledCompiler(Compiled(this, nullTyped())) { rhsCompiled ->
			compiledCompiler(Compiled(this, typed.plus(string, rhsCompiled.typed)), ret)
		}


fun <T> Entry<T>.compiler(string: String, compiled: Compiled<T>, ret: Ret<Compiled<T>>): Compiler? =
	notNullIf(string == this.string) {
		compile(compiled, ret)
	}
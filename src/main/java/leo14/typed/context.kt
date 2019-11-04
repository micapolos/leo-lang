package leo14.typed

import leo.base.notNullIf
import leo13.Stack
import leo13.mapFirst
import leo14.Compiler
import leo14.Literal
import leo14.Ret
import leo14.lambda.Term

data class Context<T>(
	val entryStack: Stack<Entry<T>>,
	val literalTerm: (Literal) -> Term<T>)

data class Entry<T>(val string: String, val compile: (Compiled<T>, Ret<Compiled<T>>) -> Compiler)

fun <T> Context<T>.compiler(string: String, typed: Typed<T>, ret: Ret<Compiled<T>>): Compiler =
	entryStack
		.mapFirst { compiler(string, Compiled(this@compiler, typed), ret) }
		?: compiledCompiler(Compiled(this, emptyTyped())) { rhsCompiled ->
			compiledCompiler(Compiled(this, typed.resolvePlus(string, rhsCompiled.typed)), ret)
		}

fun <T> Entry<T>.compiler(string: String, compiled: Compiled<T>, ret: Ret<Compiled<T>>): Compiler? =
	notNullIf(string == this.string) {
		compile(compiled, ret)
	}
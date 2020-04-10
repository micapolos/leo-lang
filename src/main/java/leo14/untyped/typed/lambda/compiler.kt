package leo14.untyped.typed.lambda

import leo.stak.reverseStack
import leo13.fold
import leo14.lambda2.fn
import leo14.lambda2.invoke

data class Compiler(
	val library: Library,
	val compiled: Compiled)

fun Library.compiler(compiled: Compiled) =
	Compiler(this, compiled)

fun Compiler.define(entry: Entry): Compiler =
	library.plus(entry).compiler(emptyCompiled)

val Compiler.end: Compiled
	get() =
		compiled.updateTerm {
			fold(library.scope.entryStak.reverseStack) {
				fn(this).invoke(it.compiled.term)
			}
		}

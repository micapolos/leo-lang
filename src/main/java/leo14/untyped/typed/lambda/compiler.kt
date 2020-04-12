package leo14.untyped.typed.lambda

import leo.stak.reverseStack
import leo13.fold
import leo14.lambda2.fn
import leo14.lambda2.invoke

data class Compiler(
	val library: Library,
	val typed: Typed)

fun Library.compiler(typed: Typed) =
	Compiler(this, typed)

fun Compiler.define(entry: Entry): Compiler =
	library.plus(entry).compiler(emptyTyped)

val Compiler.end: Typed
	get() =
		typed.updateTerm {
			fold(library.scope.entryStak.reverseStack) {
				fn(this).invoke(it.typed.term)
			}
		}

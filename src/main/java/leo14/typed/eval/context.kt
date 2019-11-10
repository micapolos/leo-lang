package leo14.typed.eval

import leo13.stack
import leo14.Script
import leo14.any
import leo14.compile
import leo14.lambda.eval.eval
import leo14.lambda.term
import leo14.ret
import leo14.typed.Typed
import leo14.typed.emptyTyped
import leo14.typed.of
import leo14.typed.plusCompiler

val Typed<Any>.eval get() = term(term.eval) of type

val Script.compileTyped: Typed<Any>
	get() =
		emptyTyped<Any>()
			.plusCompiler(stack(), { it.any }, ret())
			.compile(this)

val Script.evalTyped: Typed<Any>
	get() =
		compileTyped.eval

val Script.eval: Any
	get() =
		evalTyped.decompile

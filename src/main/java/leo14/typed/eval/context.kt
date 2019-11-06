package leo14.typed.eval

import leo13.stack
import leo14.Script
import leo14.any
import leo14.compile
import leo14.lambda.term
import leo14.ret
import leo14.typed.Context
import leo14.typed.Typed
import leo14.typed.emptyTyped
import leo14.typed.plusCompiler

val evalContext = Context(stack()) { term(it.any) }

val Script.eval: Any
	get() =
		emptyTyped<Any>()
			.plusCompiler(stack(), { it.any }, ret())
			.compile<Typed<Any>>(this)
			.decompile

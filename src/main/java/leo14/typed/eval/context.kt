package leo14.typed.eval

import leo.base.failIfOr
import leo13.stack
import leo14.Script
import leo14.any
import leo14.compile
import leo14.lambda.eval.eval
import leo14.lambda.term
import leo14.ret
import leo14.typed.*

val evalContext = Context(stack()) { term(it.any) }

val Script.eval: Any
	get() =
		compiledCompiler(evalContext.with(emptyTyped()), ret())
			.compile<Compiled<Any>>(this)
			.typed
			.run { failIfOr(type != nativeType) { term.eval } }

package leo14.untyped.typed.lambda

import leo14.Script

val Script.eval: Script
	get() =
		emptyCompiler.plus(this).let { compiler ->
			compiler.typed.eval.script(compiler.library.scope)
		}
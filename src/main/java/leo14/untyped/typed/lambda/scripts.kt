package leo14.untyped.typed.lambda

import leo14.Script
import leo14.untyped.dsl2.F
import leo14.untyped.dsl2.script_

fun eval_(f: F) {
	script_(f).eval
}

val Script.eval: Script
	get() =
		emptyCompiler.plus(this).let { compiler ->
			compiler.compiledTyped.eval.script(compiler.library.scope)
		}
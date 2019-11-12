package leo14

import leo14.typed.anyDecompile
import leo14.typed.anyEval
import leo14.typed.compiler.TypedCompiler
import leo14.typed.compiler.compile
import leo14.typed.compiler.compiler

val Script.eval
	get() =
		compiler<Any>(leo14.typed.typed())
			.compile(this)
			.run { this as TypedCompiler }
			.typed
			.anyEval
			.anyDecompile
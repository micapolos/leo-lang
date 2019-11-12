package leo14

import leo14.typed.anyDecompile
import leo14.typed.anyEval
import leo14.typed.compiler.TypedCompiler
import leo14.typed.compiler.compile
import leo14.typed.compiler.compiler
import leo14.typed.typed

val Script.eval
	get() =
		compiler(typed())
			.compile(this)
			.run { this as TypedCompiler }
			.typed
			.anyEval
			.anyDecompile
package leo14

import leo14.typed.anyDecompile
import leo14.typed.anyEval
import leo14.typed.compiler.compile
import leo14.typed.compiler.compiler
import leo14.typed.compiler.typed
import leo14.typed.typed

val Script.eval
	get() =
		compiler(typed())
			.compile(this)
			.typed
			.anyEval
			.anyDecompile
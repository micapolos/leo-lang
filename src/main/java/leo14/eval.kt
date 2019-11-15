package leo14

import leo14.typed.anyDecompile
import leo14.typed.anyEval
import leo14.typed.compiler.*
import leo14.typed.typed

val Script.eval
	get() =
		compiler(typed())
			.compile(this)
			.typed
			.anyEval
			.anyDecompile

fun Script.evalUsing(dictionary: Dictionary) =
	TypedCompiler(null, Context(dictionary, memory(), anyResolver, anyLiteralCompile), typed())
		.compile(this)
		.typed
		.anyEval
		.anyDecompile

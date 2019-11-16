package leo14

import leo14.typed.*
import leo14.typed.compiler.*

val String.eval: String
	get() =
		compile(this).nativeEval.nativeDecompile.toString()

val Script.eval: Script
	get() =
		compiler(typed())
			.compile(this)
			.typed
			.anyEval
			.anyDecompile

fun Script.evalUsing(dictionary: Dictionary) =
	TypedCompiler(null, Context(dictionary, memory(), anyResolve, anyLiteralCompile), typed())
		.compile(this)
		.typed
		.anyEval
		.anyDecompile

package leo14.typed.compiler.js

import leo14.lambda.js.expr.Expr
import leo14.lambda.native
import leo14.typed.Typed
import leo14.typed.compiler.CompiledParser
import leo14.typed.compiler.CompilerKind
import leo14.typed.compiler.compiled
import leo14.typed.compiler.compiler

val emptyCompiler =
	compiler(
		CompiledParser(
			null,
			CompilerKind.COMPILER,
			emptyContext,
			compiled()))

val Typed<Expr>.expr: Expr
	get() =
		term.native
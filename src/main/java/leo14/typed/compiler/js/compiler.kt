package leo14.typed.compiler.js

import leo14.Script
import leo14.js.ast.Expr
import leo14.lambda.js.expr
import leo14.typed.Typed
import leo14.typed.compiler.*

val emptyCompiler =
	compiler(
		CompiledParser(
			null,
			CompilerKind.COMPILER,
			emptyContext,
			compiled()))

val Script.compileTyped: Typed<Expr>
	get() =
		emptyCompiler
			.parse(this)
			.run { this as CompiledParserCompiler }
			.compiledParser
			.apply { if (parent != null) error("not top level") }
			.compiled
			.typedForEnd

val Typed<Expr>.expr: Expr
	get() =
		term.expr
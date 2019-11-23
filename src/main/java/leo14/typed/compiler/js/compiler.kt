package leo14.typed.compiler.js

import leo14.Script
import leo14.js.ast.Expr
import leo14.typed.Typed
import leo14.typed.compiler.*
import leo14.typed.typed

val emptyCompiler =
	compiler(
		CompiledParser(
			null,
			emptyContext,
			Phase.COMPILER,
			Compiled(memory(), typed())))

val Script.compileTyped: Typed<Expr>
	get() =
		emptyCompiler
			.parse(this)
			.run { this as CompiledParserCompiler }
			.compiledParser
			.apply { if (parent != null) error("not top level") }
			.compiled
			.resolveForEnd
			.typed
package leo14.typed.compiler.js

import leo14.*
import leo14.lambda.js.expr.Expr
import leo14.typed.Typed
import leo14.typed.compiler.*

val Script.compileTyped: Typed<Expr>
	get() =
		emptyCompiler
			.parse(this)
			.run { this as CompiledParserCompiler }
			.compiledParser
			.apply { if (parent != null) error("not top level") }
			.compiled
			.typedForEnd

val Script.compile: Script
	get() =
		emptyContext.compile(this)

val Script.evaluate: Script
	get() =
		emptyContext.evaluate(this)

val stdScript: Script =
	script(
		"define" lineTo script(
			"window".line,
			"gives" lineTo script(
				"window" lineTo script(
					"window".literal.line,
					"native".line)),

			"window" lineTo script("native"),
			"location".line,
			"gives" lineTo script(
				"location" lineTo script(
					"given".line,
					"window".line,
					"native".line,
					"get" lineTo script("location".literal))),

			"location" lineTo script("native"),
			"href".line,
			"gives" lineTo script(
				"given".line,
				"location".line,
				"native".line,
				"get" lineTo script("href".literal),
				"text".line)))
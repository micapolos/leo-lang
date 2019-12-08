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
		defineNativeScriptLine("window", "window"),
		defineNativeGetter("window", "location", "location"),
		defineNativeTextGetter("location", "href", "href"),

		defineTextCreateElementScriptLine,
		defineCreateElementScriptLine("div", "div"),
		defineCreateElementScriptLine("span", "span"),

		defineElementSetTextScriptLine("content", "textContent"),
		defineElementSetTextScriptLine("width", "style.width"),
		defineElementSetTextScriptLine("height", "style.height"),
		defineElementSetTextScriptLine("background", "style.background"),
		defineElementSetTextScriptLine("border", "style.border"),
		defineElementSetTextScriptLine("padding", "style.padding"))

fun defineNativeScriptLine(name: String, jsName: String): ScriptLine =
	"define" lineTo script(
		name.line,
		"gives" lineTo script(
			"window" lineTo script(
				jsName.literal.line,
				"native".line)))

fun defineNativeGetter(lhs: String, name: String, jsName: String): ScriptLine =
	"define" lineTo script(
		lhs lineTo script("native"),
		name.line,
		"gives" lineTo script(
			name lineTo script(
				"given".line,
				lhs.line,
				"native".line,
				"get" lineTo script(jsName.literal))))

fun defineNativeTextGetter(lhs: String, name: String, jsName: String): ScriptLine =
	"define" lineTo script(
		lhs lineTo script("native"),
		name.line,
		"gives" lineTo script(
			"given".line,
			lhs.line,
			"native".line,
			"get" lineTo script(jsName.literal),
			"text".line))

val defineTextCreateElementScriptLine
	get() =
		"define" lineTo script(
			"text".line,
			"element".line,
			"gives" lineTo script(
				"element" lineTo script(
					"document.createElement".literal.line,
					"native".line,
					"invoke" lineTo script(
						"given".line,
						"text".line))))

fun defineCreateElementScriptLine(name: String, jsName: String) =
	"define" lineTo script(
		name.line,
		"gives" lineTo script(
			jsName.literal.line,
			"element".line))

fun defineElementSetTextScriptLine(name: String, jsName: String): ScriptLine =
	"define" lineTo script("element" lineTo script("native".line),
		"set" lineTo script(name lineTo script("text".line)),
		"gives" lineTo script(
			"element" lineTo script(
				"given".line,
				"element".line,
				"native".line,
				"set" lineTo script(
					jsName.literal.line,
					"to" lineTo script(
						"given".line,
						"set".line,
						name.line,
						"text".line)))))

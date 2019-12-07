package leo14.typed.compiler

import leo14.*
import leo14.lambda.Evaluator
import leo14.lambda.Term
import leo14.typed.DecompileLiteral
import leo14.typed.Typed
import leo14.typed.TypedLine

typealias CompiledResolve<T> = Compiled<T>.() -> Compiled<T>?
typealias TypedResolve<T> = Typed<T>.() -> Typed<T>?
typealias TermDecompile<T> = Term<T>.() -> ScriptLine

data class Context<T>(
	val language: Language,
	val compiledResolve: CompiledResolve<T>,
	val literalCompile: LiteralCompile<T>,
	val evaluator: Evaluator<T>,
	val typeContext: TypeContext,
	val decompileLiteral: DecompileLiteral<T>,
	val termDecompile: TermDecompile<T>,
	val nativeScriptLine: T.() -> ScriptLine,
	val targetScript: Script) {
	override fun toString() = "$reflectScriptLine"
}

fun <T> Context<T>.compileLine(literal: Literal): TypedLine<T> =
	literal.literalCompile()

fun <T> Context<T>.resolve(compiled: Compiled<T>): Compiled<T>? =
	compiledResolve.invoke(compiled)

val <T> Context<T>.reflectScriptLine
	get() =
		"context" lineTo script(
			"target" lineTo targetScript,
			language.reflectScriptLine)

fun <T> Context<T>.compile(script: Script): Script =
	(compiler().parse(script) as CompiledParserCompiler<T>).compiledParser.decompile

fun <T> Context<T>.evaluate(script: Script): Script =
	(evaluator().parse(script) as CompiledParserCompiler<T>).compiledParser.decompile

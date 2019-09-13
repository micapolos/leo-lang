package leo13.untyped.compiler

import leo13.script.*
import leo13.untyped.compileName

data class CompileError(val script: Script) : RuntimeException()

fun <V> compileError(script: Script): V =
	throw CompileError(script)

fun <V> compileError(line: ScriptLine): V =
	compileError(script(line))

fun <V> compileError(name: String): V =
	compileError(script(name))

fun <V> compile(name: String, fn: () -> V): V =
	compile(name lineTo script(), fn)

fun <V> compile(line: ScriptLine, fn: () -> V): V =
	try {
		fn()
	} catch (error: CompileError) {
		compileError(error.script.plus(compileName lineTo script(line)))
	}

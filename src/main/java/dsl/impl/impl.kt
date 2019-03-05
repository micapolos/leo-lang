package dsl.impl

import leo.*
import leo.script.*
import leo.script.Script

fun term(word: Word, args: List<Any?>): ScriptLine =
	ScriptLine(
		word,
		args.fold(nullScript) { script, arg ->
			script.plus(arg.anyScriptLine)
		})

val List<Any?>.anyListScript: Script?
	get() =
		fold(nullScript) { script, arg ->
			script.plus(arg.anyScriptLine)
		}

val List<Any?>.anyListScriptLine: ScriptLine
	get() =
		when (size) {
			0 -> literalScriptLine
			else -> (this[0] as? String)?.wordOrNull?.let {
				term(it, slice(1 until size))
			} ?: literalScriptLine
		}

val Any?.anyScriptLine: ScriptLine
	get() =
		when (this) {
			is Boolean -> if (this) trueWord.line else falseWord.line
			is Byte -> scriptLine
			is Int -> scriptLine
			is String -> scriptLine
			is List<Any?> -> anyListScriptLine
			else -> literalScriptLine
		}

val Any?.literalScriptLine: ScriptLine
	get() =
		literalWord lineTo nullScript.plus(toString().scriptLine)

val Any?.anyScript
	get() =
		anyScriptLine.let { nullScript.plus(anyScriptLine) }

package leo13.untyped.compiler

import leo13.script.*
import leo13.untyped.*
import leo13.untyped.pattern.Pattern
import leo13.untyped.pattern.scriptLine

fun <R> Script.compileOnlyLine(fn: ScriptLine.() -> R): R =
	onlyLineOrNull.let {
		if (it != null) fn(it)
		else compileError(singleName lineTo script(lineName))
	}

fun <R> ScriptLine.compileRhs(name: String, fn: Script.() -> R): R =
	rhsOrNull(name).let {
		if (it != null) fn(it)
		else compileError(expectedName lineTo script(this.name))
	}

fun <R> Pattern.compileMatch(pattern: Pattern, fn: () -> R): R =
	if (this == pattern) fn()
	else compileError(mismatchName lineTo script(
		expectedName lineTo script(scriptLine),
		hasName lineTo script(pattern.scriptLine)))

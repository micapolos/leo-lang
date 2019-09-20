package leo13.compiler

import leo13.*
import leo13.pattern.Pattern
import leo13.pattern.bodyScript
import leo13.script.*

fun <R> Script.tracedOnlyLine(fn: ScriptLine.() -> R): R =
	trace {
		singleName lineTo script(lineName)
	}.traced {
		onlyLineOrNull.orTracedError.fn()
	}

fun <R> ScriptLine.tracedRhs(lineName: String, fn: Script.() -> R): R =
	trace {
		matchName lineTo script(
			nameName lineTo script(
				expectedName lineTo script(name),
				actualName lineTo script(lineName)))
	}.traced {
		rhsOrNull(lineName).orTracedError.fn()
	}

fun <R> Pattern.tracedMatch(pattern: Pattern, fn: () -> R): R =
	trace {
		matchName lineTo script(
			patternName lineTo script(
				expectedName lineTo bodyScript,
				actualName lineTo pattern.bodyScript))
	}.traced {
		if (this == pattern) fn()
		else tracedError()
	}

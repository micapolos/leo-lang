package leo13.untyped

import leo13.script.unsafeOnlyLine

val patternLineType: leo13.scripter.Scripter<PatternLine> = leo13.scripter.Scripter(
	"line",
	{ leo13.script.script(name lineTo patternType.bodyScript(rhs)) },
	{ unsafeOnlyLine.run { name lineTo patternType.unsafeBodyValue(rhs) } })

data class PatternLine(val name: String, val rhs: Pattern)

infix fun String.lineTo(rhs: Pattern) = PatternLine(this, rhs)
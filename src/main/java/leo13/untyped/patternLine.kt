package leo13.untyped

import leo13.script.*

data class PatternLine(val name: String, val rhs: Pattern)

infix fun String.lineTo(rhs: Pattern) = PatternLine(this, rhs)

fun PatternLine.matches(scriptLine: ScriptLine): Boolean =
	name == scriptLine.name && rhs.matches(scriptLine.rhs)

val patternLineName = "line"

val patternLineReader: Reader<PatternLine> =
	reader(patternLineName) {
		unsafeOnlyLine.run {
			name lineTo patternReader.unsafeBodyValue(rhs)
		}
	}

val patternLineWriter: Writer<PatternLine> =
	writer(patternLineName) {
		script(name lineTo patternWriter.bodyScript(rhs))
	}

package leo13.pattern

import leo13.ObjectScripting
import leo13.script.*

data class PatternLine(val name: String, val rhs: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = patternLineWriter.scriptLine(this)
}

infix fun String.lineTo(rhs: Pattern) = PatternLine(this, rhs)

fun PatternLine.matches(scriptLine: ScriptLine): Boolean =
	name == scriptLine.name && rhs.matches(scriptLine.rhs)

fun PatternLine.contains(line: PatternLine) =
	name == line.name && rhs.contains(rhs)

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

val PatternLine.staticScriptLineOrNull: ScriptLine?
	get() =
		rhs.staticScriptOrNull?.let { name lineTo it }

fun patternLine(scriptLine: ScriptLine) =
	scriptLine.name lineTo pattern(scriptLine.rhs)

fun PatternLine.leafPlusOrNull(pattern: Pattern): PatternLine? =
	rhs.leafPlusOrNull(pattern)?.let { name lineTo it }
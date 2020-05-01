package leo16

import leo13.Stack
import leo13.map
import leo13.stack
import leo13.zipFoldOrNull
import leo15.anyName
import leo15.functionName
import leo15.patternName
import leo15.structName

sealed class Pattern {
	override fun toString() = asSentence.toString()
}

object AnyPattern : Pattern()
object FunctionPattern : Pattern()

data class StructPattern(val struct: PatternStruct) : Pattern() {
	override fun toString() = super.toString()
}

data class PatternStruct(val lineStack: Stack<PatternLine>) {
	override fun toString() = asSentence.toString()
}

data class PatternLine(val word: String, val pattern: Pattern) {
	override fun toString() = asSentence.toString()
}

val anyPattern: Pattern = AnyPattern
val functionPattern: Pattern = FunctionPattern
val PatternStruct.pattern: Pattern get() = StructPattern(this)
val Stack<PatternLine>.struct get() = PatternStruct(this)
fun struct(vararg patternLines: PatternLine) = stack(*patternLines).struct
fun pattern(vararg patternLines: PatternLine) = struct(*patternLines).pattern
val String.pattern get() = pattern(invoke(pattern()))
operator fun String.invoke(pattern: Pattern): PatternLine = PatternLine(this, pattern)

val Pattern.asSentence: Sentence
	get() =
		patternName(asScript)

val Pattern.asScript: Script
	get() =
		when (this) {
			AnyPattern -> script(anyName())
			FunctionPattern -> script(functionName())
			is StructPattern -> struct.asScript
		}

val PatternStruct.asSentence: Sentence
	get() =
		structName(asScript)

val PatternStruct.asScript: Script
	get() =
		lineStack.map { asSentence }.script

val PatternLine.asSentence: Sentence
	get() =
		word(pattern.asScript)

fun Value.matches(pattern: Pattern): Boolean =
	when (pattern) {
		AnyPattern -> true
		FunctionPattern -> this is FunctionValue
		is StructPattern -> this is StructValue && struct.matches(pattern.struct)
	}

fun Struct.matches(patternStruct: PatternStruct): Boolean =
	true
		.zipFoldOrNull(lineStack, patternStruct.lineStack) { line, patternLine ->
			line.matches(patternLine)
		}
		?: false

fun Line.matches(line: PatternLine): Boolean =
	word == line.word && value.matches(line.pattern)

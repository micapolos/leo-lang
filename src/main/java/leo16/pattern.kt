package leo16

import leo13.Stack
import leo13.stack
import leo13.zipFoldOrNull

sealed class Pattern
object AnyPattern : Pattern()
object FunctionPattern : Pattern()
data class StructPattern(val struct: PatternStruct) : Pattern()
data class PatternStruct(val lineStack: Stack<PatternLine>)
data class PatternLine(val word: String, val pattern: Pattern)

val anyPattern: Pattern = AnyPattern
val functionPattern: Pattern = FunctionPattern
val PatternStruct.pattern: Pattern get() = StructPattern(this)
val Stack<PatternLine>.struct get() = PatternStruct(this)
fun struct(vararg patternLines: PatternLine) = stack(*patternLines).struct
fun pattern(vararg patternLines: PatternLine) = struct(*patternLines).pattern
operator fun String.invoke(pattern: Pattern): PatternLine = PatternLine(this, pattern)

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

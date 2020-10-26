package leo20

import leo13.Stack
import leo13.stack
import leo13.zipFoldOrNull
import leo14.ScriptField
import leo14.ScriptLine

sealed class Pattern
object AnyPattern : Pattern()
data class StructPattern(val lineStack: Stack<PatternLine>) : Pattern()
data class PatternLine(val name: String, val rhs: Pattern)

val anyPattern: Pattern = AnyPattern
fun pattern(vararg lines: PatternLine): Pattern = StructPattern(stack(*lines))
infix fun String.fieldTo(rhs: Pattern) = PatternLine(this, rhs)

val Pattern.isAny get() = this is AnyPattern

fun Value.matches(pattern: Pattern): Boolean =
	when (pattern) {
		AnyPattern -> true
		is StructPattern -> true
			.zipFoldOrNull(lineStack, pattern.lineStack) { line, patternLine ->
				this && line.matches(patternLine)
			} ?: false
	}

fun Line.matches(patternLine: PatternLine): Boolean =
	when (this) {
		is FieldLine -> field.matches(patternLine)
		is StringLine -> patternLine.name == "text" && patternLine.rhs.isAny
		is NumberLine -> patternLine.name == "number" && patternLine.rhs.isAny
		is FunctionLine -> patternLine.name == "function" && patternLine.rhs.isAny
	}

fun Field.matches(patternLine: PatternLine): Boolean =
	name == patternLine.name && rhs.matches(patternLine.rhs)

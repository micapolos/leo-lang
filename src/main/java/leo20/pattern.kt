package leo20

import leo13.Stack
import leo13.isEmpty
import leo13.push
import leo13.stack
import leo13.zipFold

data class Pattern(val startsWithAny: Boolean, val lineStack: Stack<PatternLine>)
data class PatternLine(val name: String, val rhs: Pattern)

val anyPattern = Pattern(true, stack())
fun pattern(vararg lines: PatternLine) = Pattern(false, stack(*lines))
fun Pattern.plus(line: PatternLine) = copy(lineStack = lineStack.push(line))
infix fun String.lineTo(rhs: Pattern) = PatternLine(this, rhs)
val Pattern.isEmpty get() = !startsWithAny && lineStack.isEmpty
val Pattern.isAny get() = startsWithAny && lineStack.isEmpty

fun Value.matches(pattern: Pattern): Boolean =
	true.zipFold(lineStack, pattern.lineStack) { lineOrNull, patternLineOrNull ->
		this && if (patternLineOrNull == null)
			if (lineOrNull == null) false
			else pattern.startsWithAny
		else
			if (lineOrNull == null) false
			else lineOrNull.matches(patternLineOrNull)
	}

fun Line.matches(patternLine: PatternLine): Boolean =
	when (this) {
		is FieldLine -> field.matches(patternLine)
		is BigDecimalLine -> patternLine.name == "number" && patternLine.rhs.isAny
		is StringLine -> patternLine.name == "text" && patternLine.rhs.isAny
		is FunctionLine -> patternLine.name == "function" && patternLine.rhs.isAny
	}

fun Field.matches(patternLine: PatternLine): Boolean =
	name == patternLine.name && rhs.matches(patternLine.rhs)

val textPatternLine = "text" lineTo anyPattern
val numberPatternLine = "number" lineTo anyPattern
val functionPatternLine = "function" lineTo anyPattern

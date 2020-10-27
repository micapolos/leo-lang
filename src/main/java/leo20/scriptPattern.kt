package leo20

import leo.base.applyOrNull
import leo.base.orNullFold
import leo.base.reverse
import leo13.push
import leo13.stack
import leo14.FieldScriptLine
import leo14.LiteralScriptLine
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.lineSeq
import leo14.script

val Script.patternOrNull: Pattern?
	get() =
		if (this == script("any")) AnyPattern
		else structPatternOrNull

val Script.structPatternOrNull: StructPattern?
	get() =
		StructPattern(stack()).orNullFold(lineSeq.reverse) { plusOrNull(it) }

fun StructPattern.plusOrNull(scriptLine: ScriptLine): StructPattern? =
	lineStack.applyOrNull(scriptLine.patternLineOrNull) { push(it) }?.let { StructPattern(it) }

val ScriptLine.patternLineOrNull: PatternLine?
	get() =
		when (this) {
			is LiteralScriptLine -> null
			is FieldScriptLine -> field.patternLineOrNull
		}

val ScriptField.patternLineOrNull
	get() =
		string.applyOrNull(rhs.patternOrNull) { fieldTo(it) }
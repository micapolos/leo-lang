package leo20

import leo.base.applyOrNull
import leo.base.orNullFold
import leo.base.reverse
import leo14.FieldScriptLine
import leo14.LiteralScriptLine
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.lineSeq
import leo14.lineTo
import leo14.script

val Script.patternOrNull: Pattern?
	get() =
		pattern().orNullFold(lineSeq.reverse) { plusOrNull(it) }

fun Pattern.plusOrNull(scriptLine: ScriptLine): Pattern? =
	if (scriptLine == "any" lineTo script() && isEmpty) anyPattern
	else applyOrNull(scriptLine.patternLineOrNull) { plus(it) }

val ScriptLine.patternLineOrNull: PatternLine?
	get() =
		when (this) {
			is LiteralScriptLine -> null
			is FieldScriptLine -> field.patternLineOrNull
		}

val ScriptField.patternLineOrNull
	get() =
		string.applyOrNull(rhs.patternOrNull) { lineTo(it) }
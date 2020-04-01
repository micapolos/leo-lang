package leo14.untyped

import leo.base.fold
import leo13.fold
import leo13.push
import leo13.stack
import leo14.*

val Script.reflectLine: ScriptLine
	get() =
		"script" lineTo script(
			"line" lineTo script(
				"list" lineTo script().fold(stack<ScriptLine>().fold(lineSeq) { push(it) }) {
					plus(it.reflectLine)
				}))

val ScriptLine.reflectLine: ScriptLine
	get() =
		when (this) {
			is LiteralScriptLine -> this
			is FieldScriptLine -> field.reflectLine
		}

val ScriptField.reflectLine: ScriptLine
	get() =
		"field" lineTo script(
			string.wordReflectLine,
			rhs.reflectLine)

val String.wordReflectLine: ScriptLine
	get() =
		"name" lineTo script(literal(this))
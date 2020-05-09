package leo16

import leo.base.fold
import leo.base.map
import leo.base.reverse
import leo14.FieldScriptLine
import leo14.LiteralScriptLine
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.lineSeq

val Script.asValue: Value
	get() =
		value().fold(lineSeq.map { field }.reverse) { plus(it) }

val ScriptLine.field: Field
	get() =
		when (this) {
			is LiteralScriptLine -> literal.asField
			is FieldScriptLine -> field.asField
		}

val ScriptField.asField: Field
	get() =
		sentence.field

val ScriptField.sentence: Sentence
	get() =
		string.sentenceTo(rhs.asValue)

package leo16

import leo.base.fold
import leo.base.map
import leo.base.reverse
import leo14.*

val Script.asValue: Value
	get() =
		value().fold(lineSeq.map { field }.reverse) { plus(it) }

val ScriptLine.field: Field
	get() =
		when (this) {
			is LiteralScriptLine -> literal.asField
			is FieldScriptLine -> field.sentence.field
		}

val ScriptField.sentence: Sentence
	get() =
		string.sentenceTo(rhs.asValue)

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
		emptyValue.fold(lineSeq.map { asSentence }.reverse) { plus(it) }

val ScriptLine.asSentence: Sentence
	get() =
		when (this) {
			is LiteralScriptLine -> literal.asSentence
			is FieldScriptLine -> field.asSentence
		}

val ScriptField.asSentence: Sentence
	get() =
		string.sentenceTo(rhs.asValue)

package leo16

import leo.base.fold
import leo.base.ifOrNull
import leo.base.map
import leo.base.reverse
import leo.base.reverseStack
import leo13.mapOrNull
import leo14.FieldScriptLine
import leo14.LiteralScriptLine
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.fieldOrNull
import leo14.lineSeq
import leo14.rhsOrNull
import leo16.names.*

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
		null
			?: listFieldOrNull
			?: sentence.field

val ScriptField.sentence: Sentence
	get() =
		string.sentenceTo(rhs.asValue)

fun ScriptField.listFieldOrNull(name: String): Field? =
	ifOrNull(string == name) {
		rhs
			.lineSeq
			.reverseStack.mapOrNull { fieldOrNull?.rhsOrNull(_item)?.asValue }
			?.field(name)
	}

val ScriptField.listFieldOrNull: Field?
	get() =
		listFieldOrNull(string)
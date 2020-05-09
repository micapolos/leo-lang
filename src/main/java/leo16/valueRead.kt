package leo16

import leo.base.ifOrNull
import leo13.linkOrNull
import leo13.mapOrNull
import leo13.push
import leo16.names.*

val Field.read: Field?
	get() =
		null
			?: listFieldOrNull
			?: this

fun Sentence.readListFieldOrNull(name: String): Field? =
	ifOrNull(word == name) {
		value
			.fieldStack
			.mapOrNull { matchPrefix(_next) { it } }
			?.linkOrNull
			?.run { stack.push(value) }
			?.field(name)
	}

val Sentence.readListOrNull: Field?
	get() =
		readListFieldOrNull(word)

val Field.listFieldOrNull: Field?
	get() =
		sentenceOrNull?.readListOrNull

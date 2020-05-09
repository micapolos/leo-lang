package leo16

import leo.base.ifOrNull
import leo13.linkOrNull
import leo13.mapOrNull
import leo13.push
import leo16.names.*

fun Sentence.listFieldOrNull(name: String): Field? =
	ifOrNull(word == name) {
		value
			.fieldStack
			.mapOrNull { matchPrefix(_item) { it } }
			?.linkOrNull
			?.run { stack.push(value) }
			?.field(name)
	}

val Sentence.listFieldOrNull: Field?
	get() =
		listFieldOrNull(word)

val Field.listFieldOrNull: Field?
	get() =
		sentenceOrNull?.listFieldOrNull

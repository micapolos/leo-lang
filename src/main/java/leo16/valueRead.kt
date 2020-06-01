package leo16

import leo13.linkOrNull
import leo13.mapOrNull
import leo13.push
import leo16.names.*

val Sentence.read: Sentence
	get() =
		null
			?: listFieldOrNull
			?: this

val Sentence.listFieldOrNull: Sentence?
	get() =
		matchPrefix(_list) { rhs ->
			rhs
				.sentenceStackOrNull
				?.mapOrNull { matchPrefix(_item) { it } }
				?.linkOrNull
				?.run { stack.push(value) }
				?.valueField
		}

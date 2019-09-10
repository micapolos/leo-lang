package leo13

import leo.base.nullOf
import leo13.generic.List
import leo13.generic.foldOrNull
import leo13.generic.list
import leo13.generic.orNullPlus

data class Given(val previousListOrNull: List<Previous>?)

fun given() = Given(null)

fun given(previous: Previous, vararg previouses: Previous) =
	Given(list(previous, *previouses))

infix fun Given.plus(previous: Previous) =
	Given(previousListOrNull.orNullPlus(previous))

val Given.sentenceStart
	get() =
		start(
			givenWord,
			nullOf<SentenceStart>()
				.foldOrNull(previousListOrNull) {
					start(previousWord, this)
				})

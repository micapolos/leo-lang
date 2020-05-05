package leo16.library.dictionary

import leo15.dsl.*
import leo16.dictionary_
import leo16.run_

val base = dictionary_ {
	core.dictionary.export
	number.dictionary.export
	int.dictionary.export
	text.dictionary.export
	list.dictionary.export
}

fun main() = run_ { base }

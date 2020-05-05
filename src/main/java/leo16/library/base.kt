package leo16.library

import leo15.dsl.*
import leo16.dictionary_
import leo16.run_

val base = dictionary_ {
	core.export
	number.export
	int.export
	text.export
	list.export
	url.export
}

fun main() = run_ { base }

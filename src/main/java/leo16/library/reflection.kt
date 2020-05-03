package leo16.library

import leo15.dsl.*
import leo16.dictionary_
import leo16.run_

val reflection = dictionary_ {
	native.reflection.export
}

fun main() = run_ { reflection }

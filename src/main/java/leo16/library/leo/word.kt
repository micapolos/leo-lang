package leo16.library.leo

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	word.value.print
}

val word = compile_ {
	use { list.library }

	any.word
	plus { any.letter }
	does {
		word.list
		append { plus.letter }
		word
	}
}
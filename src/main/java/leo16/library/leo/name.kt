package leo16.library.leo

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	name.value.print
}

val name = compile_ {
	use { list.library }

	any.name
	plus { any.letter }
	does {
		name.list
		append { plus.letter }
		name
	}
}
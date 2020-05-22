package leo16.library.native

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	character.value.print
}

val character = compile_ {
	use { reflection }

	character.class_
	is_ { "java.lang.Character".text.name.class_ }

	character.word { is_ { letter } }.method
	is_ {
		character.class_
		method {
			name { "isLetter".text }
			parameter { list { item { char.class_ } } }
		}
	}
}
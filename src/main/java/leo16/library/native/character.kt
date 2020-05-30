package leo16.library.native

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(character)
}

val character = dsl_ {
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
package leo16.library

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	character.value.print
}

val character = compile_ {
	use { reflection.library }
	use { string.native.library }
	use { character.native.library }

	native.character.text
	does { text.character.native.object_.string.text }

	native.character.reflect
	does { character { reflect.character.text } }

	native.character.letter.boolean
	does {
		character.word { is_ { letter } }.method
		invoke { parameter { list { item { boolean.letter.character.native } } } }
		boolean
	}
}
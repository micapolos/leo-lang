package leo16.library

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	character.value.print
}

val character = compile_ {
	use { reflection }
	use { string.native }
	use { character.native }

	character.any.is_ { native.any.character }

	character.any.text
	does { text.character.native.object_.string.text }

	character.any.reflect
	does { character { reflect.character.text } }

	character.any.letter.boolean
	does {
		character.word { is_ { letter } }.method
		invoke { parameter { list { item { boolean.letter.character.native } } } }
		boolean
	}
}
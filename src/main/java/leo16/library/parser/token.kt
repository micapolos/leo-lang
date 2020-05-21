package leo16.library.parser

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	token.value.print
}

val token = compile_ {
	empty.token.parser
	plus { any.character }
	does {

		plus.character.digit
		equals_ { plus.character.word { digit } }
		match {
			false_
		}
	}
}
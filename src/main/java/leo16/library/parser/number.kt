package leo16.library.parser

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	number.value.print
}

val number = compile_ {
	use { base.library }
	use { digit.library }

	any.number.natural.parser
	plus { any.character }
	does {
		parser.natural.number.times { 10.number }
		plus { plus.character.digit.number }
		natural.parser
	}

	test {
		123.number.natural.parser
		plus { "5".text.character }
		equals_ { 1235.number.natural.parser }
	}
}
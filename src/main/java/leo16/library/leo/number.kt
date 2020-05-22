package leo16.library.leo

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	number.value.print
}

val number = compile_ {
	use { digit }
	use { list }

	any.digit.number
	does {
		empty.list
		append { number.digit }
		link.number
	}

	test {
		four.digit.number
		equals_ {
			number {
				link {
					previous { empty.list }
					last { four.digit }
				}
			}
		}
	}

	any.number
	plus { any.digit }
	does {
		number.link.list
		append { plus.digit }
		link.number
	}
}
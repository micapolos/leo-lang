package leo16.library.leo

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	number.value.print
}

val number = compile_ {
	use { digit.library }
	use { list.library }

	any.digit.number
	does {
		empty.list
		append { number.digit }
		linked.number
	}

	test {
		four.digit.number
		equals_ {
			number {
				linked {
					previous { empty.list }
					last { four.digit }
				}
			}
		}
	}

	any.number
	plus { any.digit }
	does {
		number.linked.list
		append { plus.digit }
		linked.number
	}
}
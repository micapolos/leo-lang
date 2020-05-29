package leo16.library

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	option.value.print
}

val option = compile_ {
	anything.option.any
	does {
		option {
			no { meta { quote { any.option.content } } }
			or { the { any.option.content } }
		}
	}

	test {
		zero.or { one }.option.any
		equals_ {
			option {
				no { meta { quote { zero.or { one } } } }
				or { the { zero.or { one } } }
			}
		}
	}

	test {
		zero.the.option
		matches { zero.or { one }.option.any }
		equals_ { true_.boolean }
	}

	test {
		one.the.option
		matches { zero.or { one }.option.any }
		equals_ { true_.boolean }
	}

	test {
		zero.or { one }.no.option
		matches { zero.or { one }.option.any }
		equals_ { true_.boolean }
	}
}
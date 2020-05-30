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
			no { meta { quote { any.option.thing } } }
			or { the { any.option.thing } }
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
		zero.or { one }.option.any
		matches { zero.the.option }
		equals_ { true_.boolean }
	}

	test {
		zero.or { one }.option.any
		matches { one.the.option }
		equals_ { true_.boolean }
	}

	test {
		zero.or { one }.option.any
		matches { zero.or { one }.no.option }
		equals_ { true_.boolean }
	}
}
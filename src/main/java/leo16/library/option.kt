package leo16.library

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(option)
}

val option = dsl_ {
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
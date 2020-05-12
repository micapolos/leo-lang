package leo16.library

import leo15.dsl.*
import leo16.dictionary_

fun main() {
	core
}

val core = dictionary_ {
	any.clear does { nothing }

	comment { nothing }
	test { nothing equals_ { nothing_ } }

	comment { commenting }
	test { comment equals_ { nothing_ } }
	test { comment { anything } equals_ { nothing_ } }
	test {
		comment { start }
		x { 10.number }
		comment { middle }
		y { 20.number }
		comment { end }
		equals_ {
			x { 10.number }
			y { 20.number }
		}
	}

	comment { normalization }
	test { zero.negate.equals_ { negate { zero } } }
}

package leo16.lib.dictionary

import leo15.dsl.*
import leo16.dictionary_
import leo16.run_

val core = dictionary_ {
	any.clear.gives { nothing }

	comment { nothing }
	test { nothing.gives { nothing_ } }

	comment { commenting }
	test { comment.gives { nothing_ } }
	test { comment { anything }.gives { nothing_ } }
	test {
		comment { start }
		x { 10.number }
		comment { middle }
		y { 20.number }
		comment { end }
		gives {
			x { 10.number }
			y { 20.number }
		}
	}

	comment { normalization }
	test { zero.negate.gives { negate { zero } } }
}

fun main() = run_ { core }

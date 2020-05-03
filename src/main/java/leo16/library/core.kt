package leo16.library

import leo15.dsl.*
import leo16.dictionary_

val core = dictionary_ {
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
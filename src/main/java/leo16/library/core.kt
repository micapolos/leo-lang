package leo16.library

import leo15.dsl.*
import leo16.dictionary_

val core = dictionary_ {
	any.clear does { nothing }

	comment { nothing }
	test { nothing does { nothing_ } }

	comment { commenting }
	test { comment does { nothing_ } }
	test { comment { anything } does { nothing_ } }
	test {
		comment { start }
		x { 10.number }
		comment { middle }
		y { 20.number }
		comment { end }
		does {
			x { 10.number }
			y { 20.number }
		}
	}

	comment { normalization }
	test { zero.negate.does { negate { zero } } }
}

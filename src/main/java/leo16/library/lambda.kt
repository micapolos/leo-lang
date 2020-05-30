package leo16.library

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(lambda)
}

val lambda = dsl_ {
	use { number }

	term.any.is_ {
		value { anything }
		or { at { number.any } }
		or { lambda { term.repeating } }
		or { term.repeating.apply { term.repeating } }
		term
	}

	test { term.any.matches { zero.value.term } }
	test { term.any.matches { 2.number.at.term } }
	test { term.any.matches { zero.value.term.lambda.term } }
	test { term.any.matches { zero.value.term.apply { one.value.term }.term } }
}
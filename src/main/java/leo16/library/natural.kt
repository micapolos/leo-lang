package leo16.library

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(natural)
}

val natural = dsl_ {
	natural.any.is_ {
		do_ {
			zero.natural
			or { lazy_ { repeat }.next }
		}
	}

	test { natural.any.matches { zero.natural } }
	test { natural.any.matches { zero.natural.next } }
	test { natural.any.matches { zero.natural.next.next } }
}
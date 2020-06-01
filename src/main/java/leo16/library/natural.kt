package leo16.library

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(natural)
}

val natural = dsl_ {
	use { leo }

	natural.any.is_ {
		do_ {
			zero.natural
			or { lazy_ { repeat }.next }
		}
	}

	test { natural.any.matches { zero.natural } }
	test { natural.any.matches { zero.natural.next } }
	test { natural.any.matches { zero.natural.next.next } }

	natural.any.previous
	does {
		previous.thing.equals_ { zero.natural }
		match {
			true_ { previous }
			false_ { previous.thing.thing }
		}
	}

	test { zero.natural.previous.equals_ { zero.natural.previous } }
	test { zero.natural.next.previous.equals_ { zero.natural } }
	test { zero.natural.next.next.previous.equals_ { zero.natural.next } }

	natural.any
	plus { natural.any }
	does {
		lhs.is_ { thing.tail }
		rhs.is_ { plus.thing }
		rhs.equals_ { zero.natural }
		match {
			true_ { lhs }
			false_ {
				lhs.next
				plus { rhs.previous }
				repeat
			}
		}
	}

	test { zero.natural.plus { zero.natural }.equals_ { zero.natural } }
	test { zero.natural.next.plus { zero.natural }.equals_ { zero.natural.next } }
	test { zero.natural.plus { zero.natural.next }.equals_ { zero.natural.next } }
	test { zero.natural.next.plus { zero.natural.next }.equals_ { zero.natural.next.next } }
}